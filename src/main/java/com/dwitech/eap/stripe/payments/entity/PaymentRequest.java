package com.dwitech.eap.stripe.payments.entity;

import java.util.UUID;

public class PaymentRequest extends TraceableElement{
    public PaymentRequest() {}
    public PaymentRequest(final double amount, final String currency) {
        this.amount = amount;
        this.currency = currency;
        this.correlationId = UUID.randomUUID().toString();
    }
    private double amount;
    private String currency;

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
}
