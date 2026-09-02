ALTER TABLE restaurant_orders ADD COLUMN payment_method VARCHAR(20) NULL AFTER status;
UPDATE restaurant_orders SET payment_method='CASH' WHERE payment_method IS NULL;
ALTER TABLE restaurant_orders MODIFY COLUMN payment_method VARCHAR(20) NOT NULL;
UPDATE products SET active=FALSE;
INSERT INTO products(product_code,name_en,name_bn,price,display_order,active) VALUES
('N001','Chotpoti','চটপটি',50.00,1,TRUE),
('N002','Malai Chop','মালাই চপ',30.00,2,TRUE),
('N003','Cha','চা',30.00,3,TRUE),
('N004','Fuchka','ফুচকা',50.00,4,TRUE),
('N005','Golap Jamun','গোলাপ জামুন',25.00,5,TRUE),
('N006','Coffee','কফি',25.00,6,TRUE),
('N007','Singara','সিঙ্গারা',30.00,7,TRUE),
('N008','Chom Chom','চমচম',25.00,8,TRUE),
('N009','Water','পানি',20.00,9,TRUE),
('N010','Buut + Piyaju','বুট + পিয়াজু',30.00,10,TRUE),
('N011','Pudding','পুডিং',30.00,11,TRUE),
('N012','Coke','কোক',20.00,12,TRUE),
('N013','Roll','রোল',30.00,13,TRUE),
('N014','Aam Vorta','আম ভর্তা',30.00,14,TRUE),
('N015','Red Bull','রেড বুল',40.00,15,TRUE),
('N016','Halim','হালিম',60.00,16,TRUE),
('N017','Aam Sotto','আমসত্ত্ব',30.00,17,TRUE),
('N018','Juice','জুস',15.00,18,TRUE),
('N019','Tehari','তেহারি',100.00,19,TRUE),
('N020','Dohi Bara','দই বড়া',30.00,20,TRUE),
('N021','Popcorn','পপকর্ন',20.00,21,TRUE),
('N022','Chitoi','চিতই',25.00,22,TRUE),
('N023','Achar','আচার',80.00,23,TRUE),
('N024','Cap Cake','কাপকেক',25.00,24,TRUE),
('N025','Jhal Muri','ঝাল মুড়ি',30.00,25,TRUE),
('N026','Korv Bread','করভ ব্রেড',20.00,26,TRUE),
('N027','Candy packet','ক্যান্ডি প্যাকেট',20.00,27,TRUE),
('N028','Chicken Sharma',' চিকেন শর্মা',60.00,28,TRUE),
('N029','Chicken Nuggets','চিকেন নাগেটস',50.00,29,TRUE),
('N030','Combo-1','ঝাল মুড়ি+চা',50.00,30,TRUE),
('N031','Combo-2','সিঙ্গারা+সাদা মিষ্টি+পানি',70.00,31,TRUE);

