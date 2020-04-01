package com.dwitech.eap.stripe.payments;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

import javax.enterprise.context.ApplicationScoped;

import static org.eclipse.microprofile.health.HealthCheckResponse.named;

@ApplicationScoped @Readiness
public class ReadinessCheck implements HealthCheck {
    @Override
    public HealthCheckResponse call() {
        return named("Stripe-Payments : readiness check").state(true).build();
    }
}