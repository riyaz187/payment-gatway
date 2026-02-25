package com.Payment.dto;

public class ServicePaymentRequest {

    private String serviceId;
    private String userId;
    private int amount; // rupees

    public String getServiceId() { return serviceId; }
    public void setServiceId(String serviceId) { this.serviceId = serviceId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public int getAmount() { return amount; }
    public void setAmount(int amount) { this.amount = amount; }
}
