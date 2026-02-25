CREATE TABLE service_orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    service_id VARCHAR(255) NOT NULL,
    user_id VARCHAR(255) NOT NULL,
    amount BIGINT NOT NULL,

    razorpay_order_id VARCHAR(255),
    razorpay_payment_id VARCHAR(255),

    status VARCHAR(50) NOT NULL,

    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE INDEX idx_rzp_order_id
ON service_orders(razorpay_order_id);

CREATE INDEX idx_user_id
ON service_orders(user_id);
