import { useEffect, useMemo, useState } from "react";
import { api } from "../api";
import ErrorBanner from "../components/ErrorBanner";
import Loading from "../components/Loading";
import type {
  Order,
  PaymentMethod,
  Product,
  Session,
} from "../types";

export default function CounterPage({
  session,
}: {
  session: Session;
}) {
  const [products, setProducts] = useState<Product[]>([]);
  const [quantities, setQuantities] = useState<Record<number, number>>({});
  const [paymentMethod, setPaymentMethod] =
    useState<PaymentMethod>("SWISH");

  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState("");

  const [createdOrder, setCreatedOrder] =
    useState<Order | null>(null);

  const [showOrder, setShowOrder] = useState(false);

  useEffect(() => {
    api
      .getProducts(session)
      .then(setProducts)
      .catch((e) => setError(e.message))
      .finally(() => setLoading(false));
  }, [session]);

  /*
   * Items sent to backend
   */
  const selectedItems = useMemo(
    () =>
      products
        .filter((p) => (quantities[p.id] ?? 0) > 0)
        .map((p) => ({
          productId: p.id,
          quantity: quantities[p.id] ?? 0,
        })),
    [products, quantities]
  );

  /*
   * Products used by Show Order popup
   */
  const selectedProducts = useMemo(
    () =>
      products
        .filter((p) => (quantities[p.id] ?? 0) > 0)
        .map((p) => ({
          ...p,
          quantity: quantities[p.id] ?? 0,
        })),
    [products, quantities]
  );

  const total = useMemo(
    () =>
      products.reduce(
        (sum, product) =>
          sum +
          Number(product.price) *
            (quantities[product.id] ?? 0),
        0
      ),
    [products, quantities]
  );

  const selectedCount = useMemo(
    () =>
      selectedProducts.reduce(
        (sum, item) => sum + item.quantity,
        0
      ),
    [selectedProducts]
  );

  function change(id: number, difference: number) {
    setQuantities((current) => ({
      ...current,
      [id]: Math.max(
        0,
        (current[id] ?? 0) + difference
      ),
    }));
  }

  async function submit() {
    if (!session.positionId) {
      setError("Counter position is missing.");
      return;
    }

    if (!selectedItems.length) {
      setError("কমপক্ষে একটি পণ্য নির্বাচন করুন।");
      return;
    }

    setSubmitting(true);
    setError("");

    try {
      const order = await api.createOrder(
        session,
        session.positionId,
        paymentMethod,
        selectedItems
      );

      setCreatedOrder(order);
      setQuantities({});
      setShowOrder(false);
    } catch (e) {
      setError(
        e instanceof Error
          ? e.message
          : "Order failed"
      );
    } finally {
      setSubmitting(false);
    }
  }

  if (loading) {
    return <Loading />;
  }

  return (
    <section className="page-section">
      {/* =========================
          HEADER
          ========================= */}

      <div className="section-heading">
        <div>
          <h2>নতুন অর্ডার</h2>
          <p>Counter {session.positionId}</p>
        </div>

        <div className="order-summary">
          <strong>{selectedCount} আইটেম</strong>
          <span>মোট: {total.toFixed(2)} SEK</span>
        </div>
      </div>

      {error && <ErrorBanner message={error} />}

      {/* =========================
          PAYMENT + SHOW ORDER
          ========================= */}

      <div className="payment-selector">
        <span>Payment</span>

        <button
          type="button"
          className={
            paymentMethod === "SWISH"
              ? "selected"
              : ""
          }
          onClick={() =>
            setPaymentMethod("SWISH")
          }
        >
          Swish
        </button>

        <button
          type="button"
          className={
            paymentMethod === "CASH"
              ? "selected"
              : ""
          }
          onClick={() =>
            setPaymentMethod("CASH")
          }
        >
          Cash
        </button>

        <button
          type="button"
          className="show-order-button"
          disabled={selectedCount === 0}
          onClick={() => setShowOrder(true)}
        >
          🛒 Show Order ({selectedCount})
        </button>
      </div>

      {/* =========================
          PRODUCT GRID
          ========================= */}

      <div className="product-grid">
        {products.map((product) => {
          const quantity =
            quantities[product.id] ?? 0;

          return (
            <article
              className="product-card"
              key={product.id}
            >
              <div>
                <h3>{product.nameBn}</h3>

                <p>{product.nameEn}</p>

                <strong className="product-price">
                  {Number(product.price).toFixed(2)} SEK
                </strong>
              </div>

              <div className="quantity-control">
                <button
                  type="button"
                  onClick={() =>
                    change(product.id, -1)
                  }
                  disabled={quantity === 0}
                >
                  −
                </button>

                <strong>{quantity}</strong>

                <button
                  type="button"
                  onClick={() =>
                    change(product.id, 1)
                  }
                >
                  +
                </button>
              </div>
            </article>
          );
        })}
      </div>

      {/* =========================
          SUBMIT BUTTON
          ========================= */}

      <div className="sticky-action">
        <button
          type="button"
          className="primary-button large"
          disabled={
            submitting ||
            selectedItems.length === 0
          }
          onClick={submit}
        >
          {submitting
            ? "অর্ডার পাঠানো হচ্ছে..."
            : `${paymentMethod} · ${total.toFixed(
                2
              )} SEK · অর্ডার সাবমিট`}
        </button>
      </div>

      {/* =========================
          SHOW CURRENT ORDER POPUP
          ========================= */}

      {showOrder && (
        <div
          className="current-order-modal-overlay"
          onClick={() => setShowOrder(false)}
        >
          <div
            className="current-order-modal"
            onClick={(event) =>
              event.stopPropagation()
            }
          >
            <button
              type="button"
              className="current-order-close"
              onClick={() =>
                setShowOrder(false)
              }
            >
              ×
            </button>

            <h2>Current Order</h2>

            <div className="current-order-items">
              {selectedProducts.map(
                (product) => (
                  <div
                    className="current-order-item"
                    key={product.id}
                  >
                    <div>
                      <strong>
                        {product.nameBn}
                      </strong>

                      <span>
                        {product.nameEn}
                      </span>
                    </div>

                    <div className="current-order-item-price">
                      <span>
                        {product.quantity} ×{" "}
                        {Number(
                          product.price
                        ).toFixed(2)}
                      </span>

                      <strong>
                        {(
                          product.quantity *
                          Number(product.price)
                        ).toFixed(2)}{" "}
                        SEK
                      </strong>
                    </div>
                  </div>
                )
              )}
            </div>

            <div className="current-order-payment">
              <span>Payment</span>
              <strong>{paymentMethod}</strong>
            </div>

            <div className="current-order-total">
              <span>Total</span>

              <strong>
                {total.toFixed(2)} SEK
              </strong>
            </div>

            <button
              type="button"
              className="primary-button current-order-done"
              onClick={() =>
                setShowOrder(false)
              }
            >
              Close
            </button>
          </div>
        </div>
      )}

      {/* =========================
          CREATED ORDER POPUP
          ========================= */}

      {createdOrder && (
        <div className="token-modal">
          <div className="token-card">
            <p>অর্ডার সফল হয়েছে</p>

            <span>টোকেন</span>

            <strong>
              {createdOrder.tokenNumber}
            </strong>

            <small>
              {createdOrder.positionName} ·{" "}
              {createdOrder.paymentMethod}
            </small>

            <div className="token-items">
              {createdOrder.items.map(
                (item, index) => (
                  <div key={index}>
                    <span>
                      {item.quantity} ×{" "}
                      {item.nameBn}
                    </span>

                    <strong>
                      {Number(
                        item.lineTotal
                      ).toFixed(2)}{" "}
                      SEK
                    </strong>
                  </div>
                )
              )}
            </div>

            <div className="token-total">
              মোট:{" "}
              {Number(
                createdOrder.totalAmount
              ).toFixed(2)}{" "}
              SEK
            </div>

            <button
              type="button"
              className="primary-button"
              onClick={() =>
                setCreatedOrder(null)
              }
            >
              নতুন অর্ডার
            </button>
          </div>
        </div>
      )}
    </section>
  );
}