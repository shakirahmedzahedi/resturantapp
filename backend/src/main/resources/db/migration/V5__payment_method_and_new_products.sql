ALTER TABLE restaurant_orders ADD COLUMN payment_method VARCHAR(20) NULL AFTER status;
UPDATE restaurant_orders SET payment_method='CASH' WHERE payment_method IS NULL;
ALTER TABLE restaurant_orders MODIFY COLUMN payment_method VARCHAR(20) NOT NULL;
UPDATE products SET active=FALSE;
INSERT INTO products(product_code,name_en,name_bn,price,display_order,active) VALUES
('N001','Chotpoti','চটপটি',50.00,1,TRUE),
('N002','Cha','চা',30.00,2,TRUE),
('N003','Coffee','কফি',25.00,3,TRUE),
('N004','Jhal Muri','ঝাল মুড়ি',25.00,4,TRUE),
('N005','Buut + Piyaju','বুট + পিয়াজু',25.00,5,TRUE),
('N006','Dohi Bara','দই বড়া',30.00,6,TRUE),
('N007','Halim','হালিম',50.00,7,TRUE),
('N008','Water','পানি',15.00,8,TRUE),
('N009','Chitoi','চিতই',25.00,9,TRUE),
('N010','Macaron Bag','ম্যাকারন প্যাকেট',25.00,10,TRUE),
('N011','White Misti','সাদা মিষ্টি',25.00,11,TRUE),
('N012','Roll','রোল',25.00,12,TRUE),
('N013','Coke','কোক',20.00,13,TRUE),
('N014','Pudding','পুডিং',25.00,14,TRUE),
('N015','Aam Vorta','আম ভর্তা',30.00,15,TRUE),
('N016','Singara','সিঙ্গারা',25.00,16,TRUE),
('N017','Jambura Vorta','জাম্বুরা ভর্তা',30.00,17,TRUE),
('N018','Juice','জুস',15.00,18,TRUE),
('N019','Tehari','তেহারি',100.00,19,TRUE),
('N020','Korv Bread','করভ ব্রেড',20.00,20,TRUE),
('N021','Chom Chom','চমচম',25.00,21,TRUE),
('N022','Aam Otto','আম অট্টো',40.00,22,TRUE),
('N023','Loly','ললি',20.00,23,TRUE),
('N024','Cake','কেক',30.00,24,TRUE);
