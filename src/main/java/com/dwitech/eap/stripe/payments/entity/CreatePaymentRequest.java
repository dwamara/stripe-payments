package com.dwitech.eap.stripe.payments.entity;

import static java.util.UUID.randomUUID;

public class CreatePaymentRequest extends PaymentRequest {
    public CreatePaymentRequest() {}
    public CreatePaymentRequest(final double amount, final String currency) {
        this.amount = amount;
        this.currency = currency;
        this.correlationId = randomUUID().toString();
    }
}