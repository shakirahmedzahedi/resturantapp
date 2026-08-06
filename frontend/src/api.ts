import type {
  AdminDashboard,
  Order,
  OrderStatus,
  PaymentMethod,
  Product,
  SalesNotification,
  Session
} from "./types";

const API_BASE = import.meta.env.VITE_API_BASE_URL ?? "/api";

function authHeader(session: Session): string {
  return `Basic ${btoa(`${session.username}:${session.password}`)}`;
}

async function request<T>(
  session: Session,
  path: string,
  options: RequestInit = {}
): Promise<T> {
  const response = await fetch(`${API_BASE}${path}`, {
    ...options,
    headers: {
      Authorization: authHeader(session),
      "Content-Type": "application/json",
      ...(options.headers ?? {})
    }
  });

  if (!response.ok) {
    let message = `Request failed (${response.status})`;
    try {
      const body = await response.json();
      message = body.message ?? message;
    } catch {
      // Keep the default message.
    }
    throw new Error(message);
  }

  if (response.status === 204) {
    return undefined as T;
  }

  return response.json() as Promise<T>;
}

export const api = {
  async verifyLogin(session: Session): Promise<void> {
    await request<Product[]>(session, "/products");
  },

  getProducts(session: Session): Promise<Product[]> {
    return request(session, "/products");
  },

  getAllProducts(session: Session): Promise<Product[]> {
    return request(session, "/products/admin");
  },

  createProduct(
    session: Session,
    payload: {
      productCode: string;
      nameEn: string;
      nameBn: string;
      price: number;
      displayOrder: number;
    }
  ): Promise<Product> {
    return request(session, "/products", {
      method: "POST",
      body: JSON.stringify(payload)
    });
  },

  setProductActive(
    session: Session,
    productId: number,
    active: boolean
  ): Promise<Product> {
    return request(session, `/products/${productId}/active`, {
      method: "PATCH",
      body: JSON.stringify({ active })
    });
  },

  createOrder(
    session: Session,
    positionId: number,
    paymentMethod: PaymentMethod,
    items: Array<{ productId: number; quantity: number }>
  ): Promise<Order> {
    return request(session, "/orders", {
      method: "POST",
      body: JSON.stringify({ positionId, paymentMethod, items })
    });
  },

  getTodayOrders(
    session: Session,
    status?: OrderStatus
  ): Promise<Order[]> {
    const query = status ? `?status=${status}` : "";
    return request(session, `/orders/today${query}`);
  },

  updateOrderStatus(
    session: Session,
    orderId: number,
    status: OrderStatus
  ): Promise<Order> {
    return request(session, `/orders/${orderId}/status`, {
      method: "PATCH",
      body: JSON.stringify({ status })
    });
  },

  getAdminDashboard(session: Session, date?: string): Promise<AdminDashboard> {
    const query = date ? `?date=${encodeURIComponent(date)}` : "";
    return request(session, `/admin/dashboard${query}`);
  },

  getNotifications(session: Session, afterId: number): Promise<SalesNotification[]> {
    return request(session, `/notifications?afterId=${afterId}`);
  }
};
