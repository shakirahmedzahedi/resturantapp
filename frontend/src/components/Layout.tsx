import type { ReactNode } from "react";
import { NavLink } from "react-router-dom";
import type { Session } from "../types";
import SalesNotificationBanner from "./SalesNotificationBanner";

export default function Layout({session,onLogout,children}:{session:Session;onLogout:()=>void;children:ReactNode}) {
  return <div className="app-shell">
    <header className="topbar">
      <div><h1>রেস্টুরেন্ট টোকেন সিস্টেম</h1><p>{session.username} · {session.role}</p></div>
      <nav>
        {(session.role === "ORDER_TAKER" || session.role === "ADMIN") && <NavLink to="/counter">অর্ডার</NavLink>}
        {(session.role === "KITCHEN" || session.role === "ADMIN") && <NavLink to="/kitchen">কিচেন</NavLink>}
        {session.role === "ADMIN" && <>
          <NavLink to="/admin/orders">অর্ডার রিপোর্ট</NavLink>
          <NavLink to="/admin/items-sold">Items Sold</NavLink>
          <NavLink to="/admin/counter4">Counter 4</NavLink>
          <NavLink to="/admin">প্রোডাক্ট</NavLink>
        </>}
        <a href="/customer-display" target="_blank" rel="noreferrer">Customer Display</a>
        <button className="ghost-button" onClick={onLogout}>লগআউট</button>
      </nav>
    </header>
    <SalesNotificationBanner session={session}/>
    <main>{children}</main>
  </div>;
}
