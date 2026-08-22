import { useCallback, useEffect, useState } from "react";
import { api } from "../api";
import ErrorBanner from "../components/ErrorBanner";
import Loading from "../components/Loading";
import type { Counter4Dashboard, Order, Session } from "../types";

function stockholmToday(): string {
  const parts = new Intl.DateTimeFormat("en-CA", {
    timeZone: "Europe/Stockholm", year: "numeric", month: "2-digit", day: "2-digit"
  }).formatToParts(new Date());
  const year = parts.find((p) => p.type === "year")?.value ?? "";
  const month = parts.find((p) => p.type === "month")?.value ?? "";
  const day = parts.find((p) => p.type === "day")?.value ?? "";
  return `${year}-${month}-${day}`;
}

export default function Counter4AdminPage({ session }: { session: Session }) {
  const [dashboard, setDashboard] = useState<Counter4Dashboard | null>(null);
  const [selectedDate, setSelectedDate] = useState(stockholmToday);
  const [updatingOrderId, setUpdatingOrderId] = useState<number | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const loadDashboard = useCallback(async () => {
    try { setDashboard(await api.getCounter4Dashboard(session, selectedDate)); setError(""); }
    catch (err) { setError(err instanceof Error ? err.message : "Counter 4 dashboard could not load"); }
    finally { setLoading(false); }
  }, [session, selectedDate]);

  useEffect(() => {
    setLoading(true); loadDashboard();
    const timer = window.setInterval(loadDashboard, 3000);
    return () => window.clearInterval(timer);
  }, [loadDashboard]);

  async function changeStatus(order: Order, status: "COMPLETED" | "CANCELLED") {
    setUpdatingOrderId(order.id); setError("");
    try { await api.updateCounter4Status(session, order.id, status); await loadDashboard(); }
    catch (err) { setError(err instanceof Error ? err.message : "Status update failed"); }
    finally { setUpdatingOrderId(null); }
  }

  if (loading && !dashboard) return <Loading />;
  if (!dashboard) return <ErrorBanner message={error || "Counter 4 dashboard unavailable"} />;

  return <section className="page-section">
    <div className="section-heading admin-report-heading">
      <div><h2>Counter 4 Management</h2><p>Admin-only order management for Counter 4</p></div>
      <div className="admin-date-filter">
        <label><span>Date</span><input type="date" value={selectedDate} onChange={(e) => setSelectedDate(e.target.value)} /></label>
        <button type="button" className="secondary-button" onClick={() => setSelectedDate(stockholmToday())}>Today</button>
        <button type="button" className="secondary-button" onClick={loadDashboard}>Refresh</button>
      </div>
    </div>
    {error && <ErrorBanner message={error} />}
    <div className="dashboard-summary counter4-summary">
      <article><span>Total sales</span><strong>{Number(dashboard.totalSales).toFixed(2)} SEK</strong></article>
      <article className="swish-card"><span>Swish</span><strong>{Number(dashboard.swishSales).toFixed(2)} SEK</strong></article>
      <article className="cash-card"><span>Cash</span><strong>{Number(dashboard.cashSales).toFixed(2)} SEK</strong></article>
      <article><span>All orders</span><strong>{dashboard.totalOrders}</strong></article>
      <article><span>New</span><strong>{dashboard.newOrders}</strong></article>
      <article><span>Completed</span><strong>{dashboard.completedOrders}</strong></article>
      <article><span>Cancelled</span><strong>{dashboard.cancelledOrders}</strong></article>
    </div>
    {dashboard.orders.length === 0 ? <div className="empty-state">No Counter 4 orders for this date.</div> :
      <div className="counter4-orders">{dashboard.orders.map((order) =>
        <article className={`order-card status-${order.status.toLowerCase()}`} key={order.id}>
          <div className="order-card-header">
            <div><span>Token</span><strong>{order.tokenNumber}</strong></div>
            <div className="order-meta"><b>{order.paymentMethod}</b><span>{new Date(order.createdAt).toLocaleTimeString()}</span></div>
            <span className="status-badge">{order.status}</span>
          </div>
          <ul>{order.items.map((item) => <li key={item.productId}><strong>{item.quantity} ×</strong><span>{item.nameBn}</span></li>)}</ul>
          <div className="counter4-order-footer">
            <strong>{Number(order.totalAmount).toFixed(2)} SEK</strong>
            {order.status === "NEW" && <div className="order-actions">
              <button type="button" className="complete-button" disabled={updatingOrderId === order.id} onClick={() => changeStatus(order,"COMPLETED")}>Complete</button>
              <button type="button" className="danger-button" disabled={updatingOrderId === order.id} onClick={() => changeStatus(order,"CANCELLED")}>Cancel</button>
            </div>}
          </div>
        </article>
      )}</div>}
  </section>;
}
