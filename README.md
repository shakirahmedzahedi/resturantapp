# Restaurant Order System — Swish/Cash Edition

New functionality:
- 24 products with configured SEK prices
- SWISH or CASH selection when placing an order
- Payment method shown on token, kitchen screen and admin order details
- Admin totals split into Swish and Cash
- Notification immediately after placed non-cancelled Swish orders cross 7,000 SEK, 14,000 SEK, 21,000 SEK and every additional 7,000 SEK
- Cash orders never trigger the milestone

The upgrade is in `V5__payment_method_and_new_products.sql`. Do not edit V1-V4.

Run:
```powershell
Copy-Item .env.example .env
docker compose up -d --build
```
App: http://localhost
Swagger: http://localhost:8080/swagger-ui.html

For an existing database, keep volumes and let Flyway apply V5. For a fresh development database only, use `docker compose down -v` before starting; that permanently deletes local data.
