package com.Payment.controller;


import com.Payment.dto.ServicePaymentRequest;
import com.Payment.service.ServicePaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/service/payment")
@CrossOrigin
public class ServicePaymentController {

    private final ServicePaymentService service;

    public ServicePaymentController(ServicePaymentService service) {
        this.service = service;
    }

    @PostMapping("/initiate")
    public ResponseEntity<?> initiate(
            @RequestBody ServicePaymentRequest req) {
        try {
            return ResponseEntity.ok(
                    service.createServiceTransaction(req).toString()
            );
        } catch (Exception e) {
            return ResponseEntity
                    .status(500)
                    .body(e.getMessage());
        }
    }
}
