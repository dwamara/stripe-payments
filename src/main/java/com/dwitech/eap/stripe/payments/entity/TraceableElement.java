package com.dwitech.eap.stripe.payments.entity;
import java.io.Serializable;

public class TraceableElement implements Serializable {
    protected String correlationId;
    public static final String CORRELATION_ID_TAG = "X-Correlation-ID";

    public String getCorrelationId() {
        return correlationId;
    }
    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }
}