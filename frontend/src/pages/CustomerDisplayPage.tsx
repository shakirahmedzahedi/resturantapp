import { useCallback, useEffect, useState } from "react";
import { api } from "../api";
import type { CustomerDisplay } from "../types";

export default function CustomerDisplayPage() {
  const [display, setDisplay] = useState<CustomerDisplay | null>(null);
  const [error, setError] = useState("");

  const loadDisplay = useCallback(async () => {
    try {
      setDisplay(await api.getCustomerDisplay());
      setError("");
    } catch (err) {
      setError(err instanceof Error ? err.message : "Order status could not load");
    }
  }, []);

  useEffect(() => {
    document.title = "Order Status";
    loadDisplay();
    const timer = window.setInterval(loadDisplay, 3000);
    return () => window.clearInterval(timer);
  }, [loadDisplay]);

  return (
    <main className="customer-display-page">
      <header className="customer-display-header">
        <div>
          <h1>Order Status</h1>
          <p>অর্ডারের অবস্থা</p>
        </div>
        {display && <span>{display.businessDate}</span>}
      </header>

      {error && <div className="customer-display-error">{error}</div>}

      <div className="customer-display-columns">
        <section className="customer-status-panel received-panel">
          <div className="customer-status-heading">
            <div>
              <h2>ORDER RECEIVED</h2>
              <p>অর্ডার গ্রহণ করা হয়েছে</p>
            </div>
            <strong>{display?.received.length ?? 0}</strong>
          </div>

          <div className="customer-token-grid">
            {display?.received.map((order) => (
              <article className="customer-token received-token" key={order.id}>
                <span>Token</span>
                <strong>{order.tokenNumber}</strong>
              </article>
            ))}
          </div>

          {display && display.received.length === 0 && (
            <div className="customer-display-empty">No received orders</div>
          )}
        </section>

        <section className="customer-status-panel ready-panel">
          <div className="customer-status-heading">
            <div>
              <h2>READY</h2>
              <p>অর্ডার প্রস্তুত</p>
            </div>
            <strong>{display?.ready.length ?? 0}</strong>
          </div>

          <div className="customer-token-grid ready-token-grid">
            {display?.ready.map((order, index) => (
              <article
                className={`customer-token ready-token ${index === 0 ? "latest-ready-token" : ""}`}
                key={order.id}
              >
                <span>Token</span>
                <strong>{order.tokenNumber}</strong>
              </article>
            ))}
          </div>

          {display && display.ready.length === 0 && (
            <div className="customer-display-empty">No ready orders yet</div>
          )}
        </section>
      </div>
    </main>
  );
}
