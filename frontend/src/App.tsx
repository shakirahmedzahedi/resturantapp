import { Navigate, Route, Routes, useLocation } from "react-router-dom";
import { useState } from "react";
import type { Session } from "./types";
import { clearSession, loadSession, saveSession } from "./session";
import LoginPage from "./pages/LoginPage";
import CounterPage from "./pages/CounterPage";
import KitchenPage from "./pages/KitchenPage";
import AdminPage from "./pages/AdminPage";
import AdminOrdersPage from "./pages/AdminOrdersPage";
import CustomerDisplayPage from "./pages/CustomerDisplayPage";
import ItemsSoldPage from "./pages/ItemsSoldPage";
import Counter4AdminPage from "./pages/Counter4AdminPage";
import Layout from "./components/Layout";

export default function App() {
  const location = useLocation();
  const [session, setSession] = useState<Session | null>(() => loadSession());

  function onLogin(next: Session) {
    saveSession(next);
    setSession(next);
  }

  function onLogout() {
    clearSession();
    setSession(null);
  }

  if (location.pathname === "/customer-display") {
    return <CustomerDisplayPage />;
  }

  if (!session) {
    return <LoginPage onLogin={onLogin} />;
  }

  const defaultPath =
    session.role === "ORDER_TAKER"
      ? "/counter"
      : session.role === "KITCHEN"
        ? "/kitchen"
        : "/admin/orders";

  return (
    <Layout session={session} onLogout={onLogout}>
      <Routes>
        <Route path="/" element={<Navigate to={defaultPath} replace />} />

        <Route
          path="/counter"
          element={
            session.role === "ORDER_TAKER" || session.role === "ADMIN"
              ? <CounterPage session={session} />
              : <Navigate to={defaultPath} replace />
          }
        />

        <Route
          path="/kitchen"
          element={
            session.role === "KITCHEN" || session.role === "ADMIN"
              ? <KitchenPage session={session} />
              : <Navigate to={defaultPath} replace />
          }
        />

        <Route
          path="/admin/orders"
          element={
            session.role === "ADMIN"
              ? <AdminOrdersPage session={session} />
              : <Navigate to={defaultPath} replace />
          }
        />

        <Route
          path="/admin"
          element={
            session.role === "ADMIN"
              ? <AdminPage session={session} />
              : <Navigate to={defaultPath} replace />
          }
        />

        <Route path="/admin/items-sold" element={session.role === "ADMIN" ? <ItemsSoldPage session={session} /> : <Navigate to={defaultPath} replace />} />
        <Route path="/admin/counter4" element={session.role === "ADMIN" ? <Counter4AdminPage session={session} /> : <Navigate to={defaultPath} replace />} />

        <Route path="*" element={<Navigate to={defaultPath} replace />} />
      </Routes>
    </Layout>
  );
}
