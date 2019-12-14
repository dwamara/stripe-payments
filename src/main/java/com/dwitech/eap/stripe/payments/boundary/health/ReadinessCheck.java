package com.dwitech.eap.stripe.payments.boundary.health;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Readiness;

import javax.enterprise.context.ApplicationScoped;

import static org.eclipse.microprofile.health.HealthCheckResponse.named;

@ApplicationScoped @Readiness
public class ReadinessCheck implements HealthCheck {
    @Override
    public HealthCheckResponse call() {
        final HealthCheckResponseBuilder responseBuilder = named("Stripe-Payments-Service : readiness check");
        responseBuilder.state(true);
        return responseBuilder.build();
    }
}