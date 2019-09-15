package com.dwitech.eap.stripe.payments.entity;

public class PaymentRequest extends TraceableElement{
    PaymentRequest() {}

    double amount;
    String currency;

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
}