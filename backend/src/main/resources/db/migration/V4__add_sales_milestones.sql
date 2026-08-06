CREATE TABLE sales_milestones (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    business_date DATE NOT NULL,
    threshold_amount DECIMAL(12,2) NOT NULL,
    total_sales DECIMAL(12,2) NOT NULL,
    created_at DATETIME(6) NOT NULL,
    CONSTRAINT uk_sales_milestone UNIQUE (business_date, threshold_amount)
);
CREATE INDEX idx_sales_milestones_date_id ON sales_milestones (business_date, id);
