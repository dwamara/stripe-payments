package com.dwitech.eap.stripe.payments.entity;

import static java.util.UUID.randomUUID;

public class UpdatePaymentRequest extends PaymentRequest{
    public UpdatePaymentRequest() {}
    public UpdatePaymentRequest(final double amount, final String currency, final String payId) {
        this.amount = amount;
        this.currency = currency;
        this.payId = payId;
        this.correlationId = randomUUID().toString();
    }
    private String payId;

    public String getPayId() { return payId; }
    public void setPayId(String payId) { this.payId = payId; }
}