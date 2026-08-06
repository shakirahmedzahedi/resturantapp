import { useCallback, useEffect, useState } from "react";
import { api } from "../api";
import ErrorBanner from "../components/ErrorBanner";
import Loading from "../components/Loading";
import type { Order, Session } from "../types";

export default function KitchenPage({ session }: { session: Session }) {
  const [orders, setOrders] = useState<Order[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [updatingId, setUpdatingId] = useState<number | null>(null);

  const loadOrders = useCallback(async () => {
    try {
      // The kitchen board intentionally requests only NEW orders.
      const result = await api.getTodayOrders(session, "NEW");
      setOrders(result);
      setError("");
    } catch (err) {
      setError(err instanceof Error ? err.message : "Orders could not load");
    } finally {
      setLoading(false);
    }
  }, [session]);

  useEffect(() => {
    loadOrders();
    const timer = window.setInterval(loadOrders, 3000);
    return () => window.clearInterval(timer);
  }, [loadOrders]);

  async function update(order: Order, status: "COMPLETED" | "CANCELLED") {
    setUpdatingId(order.id);
    setError("");

    try {
      await api.updateOrderStatus(session, order.id, status);

      // Remove it immediately. The next refresh also confirms the server state.
      setOrders((current) => current.filter((item) => item.id !== order.id));
    } catch (err) {
      setError(err instanceof Error ? err.message : "Update failed");
    } finally {
      setUpdatingId(null);
    }
  }

  if (loading) return <Loading />;

  return (
    <section className="page-section">
      <div className="section-heading">
        <div>
          <h2>নতুন কিচেন অর্ডার</h2>
          <p>
            শুধু NEW অর্ডার দেখানো হয়। সম্পন্ন বা বাতিল করলে অর্ডারটি
            সঙ্গে সঙ্গে বোর্ড থেকে চলে যাবে।
          </p>
        </div>

        <div className="kitchen-summary">
          <strong>{orders.length}</strong>
          <span>নতুন অর্ডার</span>
        </div>
      </div>

      {error && <ErrorBanner message={error} />}

      <div className="order-list">
        {orders.length === 0 && (
          <div className="empty-state">
            এই মুহূর্তে কোনো নতুন অর্ডার নেই
          </div>
        )}

        {orders.map((order) => (
          <article className="order-card status-new" key={order.id}>
            <div className="order-card-header">
              <div>
                <span>টোকেন</span>
                <strong>{order.tokenNumber}</strong>
              </div>

              <div className="order-meta">
                <b>{order.positionName} · {order.paymentMethod}</b>
                <small>
                  {new Date(order.createdAt).toLocaleTimeString()}
                </small>
              </div>

              <span className="status-badge">নতুন</span>
            </div>

            <ul>
              {order.items.map((item) => (
                <li key={item.productId}>
                  <strong>{item.quantity} ×</strong>
                  <span>{item.nameBn}</span>
                </li>
              ))}
            </ul>

            <div className="order-actions">
              <button
                className="complete-button"
                disabled={updatingId === order.id}
                onClick={() => update(order, "COMPLETED")}
              >
                {updatingId === order.id ? "আপডেট হচ্ছে..." : "সম্পন্ন"}
              </button>

              <button
                className="danger-button"
                disabled={updatingId === order.id}
                onClick={() => update(order, "CANCELLED")}
              >
                বাতিল
              </button>
            </div>
          </article>
        ))}
      </div>
    </section>
  );
}
