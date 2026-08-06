UPDATE restaurant_orders
SET status = 'NEW'
WHERE status IN ('ACCEPTED', 'PREPARING', 'READY');

UPDATE restaurant_orders
SET status = 'CANCELLED'
WHERE status = 'CANCLE';
