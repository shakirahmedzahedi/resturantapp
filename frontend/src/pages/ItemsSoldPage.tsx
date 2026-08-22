import { useCallback, useEffect, useState } from "react";
import { api } from "../api";
import ErrorBanner from "../components/ErrorBanner";
import Loading from "../components/Loading";
import type { ItemSales, Session } from "../types";

function stockholmToday(): string {
  const parts = new Intl.DateTimeFormat("en-CA", {
    timeZone: "Europe/Stockholm", year: "numeric", month: "2-digit", day: "2-digit"
  }).formatToParts(new Date());
  const year = parts.find((p) => p.type === "year")?.value ?? "";
  const month = parts.find((p) => p.type === "month")?.value ?? "";
  const day = parts.find((p) => p.type === "day")?.value ?? "";
  return `${year}-${month}-${day}`;
}

export default function ItemsSoldPage({ session }: { session: Session }) {
  const [items, setItems] = useState<ItemSales[]>([]);
  const [selectedDate, setSelectedDate] = useState(stockholmToday);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const loadItems = useCallback(async () => {
    try {
      setItems(await api.getItemsSold(session, selectedDate));
      setError("");
    } catch (err) {
      setError(err instanceof Error ? err.message : "Item sales could not load");
    } finally { setLoading(false); }
  }, [session, selectedDate]);

  useEffect(() => {
    setLoading(true); loadItems();
    const timer = window.setInterval(loadItems, 5000);
    return () => window.clearInterval(timer);
  }, [loadItems]);

  if (loading && items.length === 0) return <Loading />;
  const totalQuantity = items.reduce((sum, item) => sum + item.quantity, 0);

  return <section className="page-section">
    <div className="section-heading admin-report-heading">
      <div><h2>Items Sold</h2><p>Cancelled orders are excluded. Counter 4 is included.</p></div>
      <div className="admin-date-filter">
        <label><span>Date</span><input type="date" value={selectedDate} onChange={(e) => setSelectedDate(e.target.value)} /></label>
        <button type="button" className="secondary-button" onClick={() => setSelectedDate(stockholmToday())}>Today</button>
        <button type="button" className="secondary-button" onClick={loadItems}>Refresh</button>
      </div>
    </div>
    {error && <ErrorBanner message={error} />}
    <div className="items-sold-summary">
      <article><span>Different items sold</span><strong>{items.length}</strong></article>
      <article><span>Total pieces sold</span><strong>{totalQuantity}</strong></article>
    </div>
    {items.length === 0 ? <div className="empty-state">No sold items for this date.</div> :
      <div className="items-sold-grid">{items.map((item,index) =>
        <article className="item-sold-card" key={item.productId}>
          <span className="item-rank">#{index + 1}</span>
          <div className="item-sold-name"><strong>{item.nameBn}</strong><span>{item.nameEn || item.productCode}</span></div>
          <strong className="item-sold-count">{item.quantity}</strong>
        </article>
      )}</div>}
  </section>;
}
