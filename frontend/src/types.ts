export type UserRole = "ORDER_TAKER" | "KITCHEN" | "ADMIN";
export type PaymentMethod = "SWISH" | "CASH";

export interface Session {
  username: string;
  password: string;
  role: UserRole;
  positionId?: number;
}

export interface Product {
  id: number;
  code: string;
  nameEn: string | null;
  nameBn: string;
  price: number;
  displayOrder: number;
  active: boolean;
}

export type OrderStatus = "NEW" | "COMPLETED" | "CANCELLED";

export interface OrderItem {
  productId: number;
  productCode: string;
  nameEn: string | null;
  nameBn: string;
  quantity: number;
  unitPrice: number;
  lineTotal: number;
}

export interface Order {
  id: number;
  tokenNumber: number;
  businessDate: string;
  positionCode: string;
  positionName: string;
  createdBy: string;
  status: OrderStatus;
  paymentMethod: PaymentMethod;
  totalAmount: number;
  createdAt: string;
  items: OrderItem[];
}

export interface SalesNotification {
  id: number;
  thresholdAmount: number;
  totalSales: number;
  message: string;
  createdAt: string;
}

export interface AdminDashboard {
  businessDate: string;
  totalOrders: number;
  newOrders: number;
  completedOrders: number;
  cancelledOrders: number;
  totalSales: number;
  swishSales: number;
  cashSales: number;
  orders: Order[];
}

export interface ItemSales {
  productId: number;
  productCode: string;
  nameEn: string | null;
  nameBn: string;
  quantity: number;
}

export interface Counter4Dashboard {
  businessDate: string;
  totalOrders: number;
  newOrders: number;
  completedOrders: number;
  cancelledOrders: number;
  totalSales: number;
  swishSales: number;
  cashSales: number;
  orders: Order[];
}

export interface CustomerDisplayOrder {
  id: number;
  tokenNumber: number;
  positionName: string;
  status: "NEW" | "COMPLETED";
  createdAt: string;
  updatedAt: string;
}

export interface CustomerDisplay {
  businessDate: string;
  received: CustomerDisplayOrder[];
  ready: CustomerDisplayOrder[];
}
