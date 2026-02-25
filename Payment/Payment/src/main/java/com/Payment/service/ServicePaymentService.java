package com.Payment.service;


import com.Payment.dto.ServicePaymentRequest;
import com.Payment.entity.OrderStatus;
import com.Payment.entity.ServiceOrder;
import com.Payment.repository.ServiceOrderRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ServicePaymentService {

    @Value("${razorpay.keyId}")
    private String keyId;

    @Value("${razorpay.keySecret}")
    private String keySecret;

    private final ServiceOrderRepository repo;

    public ServicePaymentService(ServiceOrderRepository repo) {
        this.repo = repo;
    }

    public JSONObject createServiceTransaction(ServicePaymentRequest req)
            throws Exception {

        // 1. Create business order
        ServiceOrder order = new ServiceOrder();
        order.setServiceId(req.getServiceId());
        order.setUserId(req.getUserId());
        order.setAmount(req.getAmount() * 100L);
        order.setStatus(OrderStatus.CREATED);
        repo.save(order);

        // 2. Create Razorpay order
        RazorpayClient client =
                new RazorpayClient(keyId, keySecret);

        JSONObject options = new JSONObject();
        options.put("amount", req.getAmount() * 100);
        options.put("currency", "INR");
        options.put("receipt", "SVC_" + order.getId());
        options.put("payment_capture", 1);

        Order rzOrder = client.orders.create(options);

        // 3. Map them
        order.setRazorpayOrderId(rzOrder.get("id"));
        order.setStatus(OrderStatus.PAYMENT_INITIATED);
        repo.save(order);

        // 4. Frontend response
        JSONObject response = new JSONObject();
        response.put("orderId", Optional.ofNullable(rzOrder.get("id")));
        response.put("serviceOrderId", order.getId());
        response.put("amount", req.getAmount());

        return response;
    }
}
