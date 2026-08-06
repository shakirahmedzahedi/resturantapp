import { useCallback, useEffect, useState } from "react";
import { api } from "../api";
import ErrorBanner from "../components/ErrorBanner";
import Loading from "../components/Loading";
import type { AdminDashboard, Order, Session } from "../types";

export default function AdminOrdersPage({ session }: { session: Session }) {
  const [dashboard, setDashboard] = useState<AdminDashboard | null>(null);
  const [selectedOrder, setSelectedOrder] = useState<Order | null>(null);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(true);

  const loadDashboard = useCallback(async () => {
    try {
      setDashboard(await api.getAdminDashboard(session));
      setError("");
    } catch (err) {
      setError(err instanceof Error ? err.message : "Dashboard could not load");
    } finally {
      setLoading(false);
    }
  }, [session]);

  useEffect(() => {
    loadDashboard();
    const timer = window.setInterval(loadDashboard, 5000);
    return () => window.clearInterval(timer);
  }, [loadDashboard]);

  if (loading) return <Loading />;
  if (!dashboard) return <ErrorBanner message={error || "Dashboard unavailable"} />;

  return (
    <section className="page-section">
      <div className="section-heading">
        <div>
          <h2>অর্ডার ও বিক্রির রিপোর্ট</h2>
          <p>{dashboard.businessDate}</p>
        </div>
        <button className="secondary-button" onClick={loadDashboard}>রিফ্রেশ</button>
      </div>

      {error && <ErrorBanner message={error} />}

      <div className="dashboard-summary dashboard-summary-wide">
        <article>
          <span>মোট অর্ডার ভ্যালু</span>
          <strong>{Number(dashboard.totalSales).toFixed(2)} SEK</strong>
        </article>
        <article className="swish-card">
          <span>Swish</span>
          <strong>{Number(dashboard.swishSales).toFixed(2)} SEK</strong>
        </article>
        <article className="cash-card">
          <span>Cash</span>
          <strong>{Number(dashboard.cashSales).toFixed(2)} SEK</strong>
        </article>
        <article><span>সব অর্ডার</span><strong>{dashboard.totalOrders}</strong></article>
        <article><span>নতুন</span><strong>{dashboard.newOrders}</strong></article>
        <article><span>সম্পন্ন</span><strong>{dashboard.completedOrders}</strong></article>
        <article><span>বাতিল</span><strong>{dashboard.cancelledOrders}</strong></article>
      </div>

      <div className="admin-orders-table-wrapper">
        <table className="admin-orders-table">
          <thead>
            <tr>
              <th>টোকেন</th><th>কাউন্টার</th><th>Payment</th><th>সময়</th>
              <th>স্ট্যাটাস</th><th>মোট</th><th></th>
            </tr>
          </thead>
          <tbody>
            {dashboard.orders.map((order) => (
              <tr key={order.id}>
                <td>{order.tokenNumber}</td>
                <td>{order.positionName}</td>
                <td>{order.paymentMethod}</td>
                <td>{new Date(order.createdAt).toLocaleTimeString()}</td>
                <td>{order.status}</td>
                <td>{Number(order.totalAmount).toFixed(2)} SEK</td>
                <td>
                  <button className="secondary-button" onClick={() => setSelectedOrder(order)}>
                    বিস্তারিত
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {selectedOrder && (
        <div className="token-modal">
          <div className="order-details-modal">
            <button className="modal-close" onClick={() => setSelectedOrder(null)}>×</button>
            <h2>টোকেন {selectedOrder.tokenNumber}</h2>
            <p>{selectedOrder.positionName} · {selectedOrder.status} · {selectedOrder.paymentMethod}</p>
            <div className="order-detail-items">
              {selectedOrder.items.map((item) => (
                <div key={item.productId}>
                  <span>{item.quantity} × {item.nameBn}</span>
                  <strong>{Number(item.lineTotal).toFixed(2)} SEK</strong>
                </div>
              ))}
            </div>
            <div className="order-detail-total">
              মোট: {Number(selectedOrder.totalAmount).toFixed(2)} SEK
            </div>
          </div>
        </div>
      )}
    </section>
  );
}
