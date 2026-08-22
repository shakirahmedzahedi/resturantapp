# Restaurant Order Backend

Spring Boot backend for the restaurant token/order system.

## Order statuses

```text
NEW
COMPLETED
CANCELLED
```

Only a `NEW` order can change to `COMPLETED` or `CANCELLED`.

For the customer-facing screen:

- `NEW` = **Received / Placed**
- `COMPLETED` = **Ready**
- `CANCELLED` is not shown

## Business day

All daily behavior uses the configured business timezone:

```text
Europe/Stockholm
```

Override it with:

```bash
RESTAURANT_BUSINESS_ZONE=Europe/Stockholm
```

This timezone controls:

- new order `businessDate`
- daily token sequence
- `/api/orders/today`
- customer display
- default Admin dashboard date
- daily sales milestone notifications

Historical orders are never deleted at midnight. The frontend simply requests the current business date for operational screens.

## Customer display API

Public read-only endpoint:

```http
GET /api/customer-display
```

Example response:

```json
{
  "businessDate": "2026-08-22",
  "received": [
    {
      "id": 15,
      "tokenNumber": 112,
      "positionName": "Counter 2",
      "status": "NEW",
      "createdAt": "2026-08-22T10:15:00",
      "updatedAt": "2026-08-22T10:15:00"
    }
  ],
  "ready": [
    {
      "id": 14,
      "tokenNumber": 111,
      "positionName": "Counter 1",
      "status": "COMPLETED",
      "createdAt": "2026-08-22T10:10:00",
      "updatedAt": "2026-08-22T10:18:00"
    }
  ]
}
```

Behavior:

- `received`: **all** today's `NEW` orders, oldest first
- `ready`: only the **10 latest** today's `COMPLETED` orders, newest first
- cancelled orders are excluded
- no authentication is required for this read-only endpoint
- no payment/user/item details are exposed

The frontend can poll this endpoint every few seconds.

## Admin date-wise report

Admin-only endpoint:

```http
GET /api/admin/dashboard?date=2026-08-22
```

If `date` is omitted, today's Stockholm business date is used:

```http
GET /api/admin/dashboard
```

The response contains, for the selected date:

- total number of orders
- NEW orders
- COMPLETED orders
- CANCELLED orders
- total non-cancelled sales
- Swish sales
- Cash sales
- complete order list for that date

This allows the frontend Admin page to use a date picker without losing historical data.

## Operational order API

```http
GET /api/orders/today
GET /api/orders/today?status=NEW
GET /api/orders/today?status=COMPLETED
```

These endpoints intentionally return only the current business day's data for Counter/Kitchen screens.
