import { FormEvent, useCallback, useEffect, useState } from "react";
import { api } from "../api";
import ErrorBanner from "../components/ErrorBanner";
import Loading from "../components/Loading";
import type { Product, Session } from "../types";

const initialForm = {
  productCode: "",
  nameEn: "",
  nameBn: "",
  price: "0",
  displayOrder: "1"
};

export default function AdminPage({ session }: { session: Session }) {
  const [products, setProducts] = useState<Product[]>([]);
  const [form, setForm] = useState(initialForm);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState("");

  const loadProducts = useCallback(async () => {
    try {
      setProducts(await api.getAllProducts(session));
      setError("");
    } catch (err) {
      setError(err instanceof Error ? err.message : "Products could not load");
    } finally {
      setLoading(false);
    }
  }, [session]);

  useEffect(() => {
    loadProducts();
  }, [loadProducts]);

  async function createProduct(event: FormEvent) {
    event.preventDefault();
    setSaving(true);
    setError("");

    try {
      await api.createProduct(session, {
        productCode: form.productCode,
        nameEn: form.nameEn,
        nameBn: form.nameBn,
        price: Number(form.price),
        displayOrder: Number(form.displayOrder)
      });
      setForm({
        ...initialForm,
        displayOrder: String(products.length + 1)
      });
      await loadProducts();
    } catch (err) {
      setError(err instanceof Error ? err.message : "Product creation failed");
    } finally {
      setSaving(false);
    }
  }

  async function toggle(product: Product) {
    try {
      await api.setProductActive(session, product.id, !product.active);
      await loadProducts();
    } catch (err) {
      setError(err instanceof Error ? err.message : "Update failed");
    }
  }

  if (loading) return <Loading />;

  return (
    <section className="page-section admin-layout">
      <div>
        <div className="section-heading">
          <div>
            <h2>প্রোডাক্ট ম্যানেজমেন্ট</h2>
            <p>নতুন পণ্য যোগ করুন অথবা চালু/বন্ধ করুন</p>
          </div>
        </div>

        {error && <ErrorBanner message={error} />}

        <form className="product-form" onSubmit={createProduct}>
          <label>
            Product Code
            <input
              required
              value={form.productCode}
              onChange={(e) => setForm({ ...form, productCode: e.target.value })}
              placeholder="P011"
            />
          </label>

          <label>
            বাংলা নাম
            <input
              required
              value={form.nameBn}
              onChange={(e) => setForm({ ...form, nameBn: e.target.value })}
              placeholder="চা"
            />
          </label>

          <label>
            English Name
            <input
              value={form.nameEn}
              onChange={(e) => setForm({ ...form, nameEn: e.target.value })}
              placeholder="Tea"
            />
          </label>

          <label>
            Price
            <input
              type="number"
              min="0"
              step="0.01"
              required
              value={form.price}
              onChange={(e) => setForm({ ...form, price: e.target.value })}
            />
          </label>

          <label>
            Display Order
            <input
              type="number"
              min="1"
              required
              value={form.displayOrder}
              onChange={(e) => setForm({ ...form, displayOrder: e.target.value })}
            />
          </label>

          <button className="primary-button" disabled={saving}>
            {saving ? "সেভ হচ্ছে..." : "প্রোডাক্ট যোগ করুন"}
          </button>
        </form>
      </div>

      <div>
        <h3 className="list-title">সব প্রোডাক্ট</h3>
        <div className="admin-product-list">
          {products.map((product) => (
            <article key={product.id}>
              <div>
                <strong>{product.nameBn}</strong>
                <span>{product.code} · {product.nameEn || "—"}</span>
              </div>
              <div className="admin-product-actions">
                <span>{product.price.toFixed(2)}</span>
                <button
                  className={product.active ? "toggle active" : "toggle"}
                  onClick={() => toggle(product)}
                >
                  {product.active ? "Active" : "Inactive"}
                </button>
              </div>
            </article>
          ))}
        </div>
      </div>
    </section>
  );
}
