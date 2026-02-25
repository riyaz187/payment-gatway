package com.Payment.webhook;


import com.Payment.entity.OrderStatus;
import com.Payment.entity.ServiceOrder;
import com.Payment.repository.ServiceOrderRepository;
import org.apache.commons.codec.digest.HmacUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/service/payment")
public class RazorpayWebhookController {

    @Value("${razorpay.webhookSecret}")
    private String webhookSecret;

    private final ServiceOrderRepository repo;

    public RazorpayWebhookController(ServiceOrderRepository repo) {
        this.repo = repo;
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> webhook(
            @RequestBody String payload,
            @RequestHeader("X-Razorpay-Signature") String signature) {

        String generated =
                HmacUtils.hmacSha256Hex(webhookSecret, payload);

        if (!generated.equals(signature)) {
            return ResponseEntity
                    .status(400)
                    .body("Invalid Signature");
        }

        JSONObject json = new JSONObject(payload);
        String event = json.getString("event");

        if ("payment.captured".equals(event)) {

            JSONObject entity = json
                    .getJSONObject("payload")
                    .getJSONObject("payment")
                    .getJSONObject("entity");

            String orderId = entity.getString("order_id");
            String paymentId = entity.getString("id");

            ServiceOrder order =
                    repo.findByRazorpayOrderId(orderId);

            if (order != null) {
                order.setRazorpayPaymentId(paymentId);
                order.setStatus(OrderStatus.PAID);
                repo.save(order);
            }
        }

        return ResponseEntity.ok("OK");
    }
}
