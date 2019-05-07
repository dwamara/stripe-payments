package com.dwitech.eap.stripe.payments.entity;

import com.google.gson.annotations.SerializedName;

public class ConfirmPaymentRequest {
    @SerializedName("payment_method_id") String paymentMethodId;
    @SerializedName("payment_intent_id") String paymentIntentId;
    @SerializedName("payment_amount") String paymentAmount;
    @SerializedName("currency") String currency;

    public String getCurrency() { return currency.toUpperCase(); }
    public String getPaymentAmount() { return paymentAmount; }
    public String getPaymentMethodId() { return paymentMethodId; }
    public String getPaymentIntentId() { return paymentIntentId; }
}
