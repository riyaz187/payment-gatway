package com.Payment.repository;

import com.Payment.entity.ServiceOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceOrderRepository
        extends JpaRepository<ServiceOrder, Long> {

    ServiceOrder findByRazorpayOrderId(String razorpayOrderId);
}
