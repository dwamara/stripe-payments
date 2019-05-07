package com.dwitech.eap.stripe.payments.boundary;

import org.eclipse.microprofile.health.Health;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;

import javax.enterprise.context.ApplicationScoped;
import java.util.Date;

import static java.lang.System.currentTimeMillis;

@Health
@ApplicationScoped
public class HealthCheckEndpoint implements HealthCheck {

    @Override
    public HealthCheckResponse call() {
        return HealthCheckResponse.named("Stripe-Payments-Service Reachable: " + new Date(currentTimeMillis())).up().build();
    }
}