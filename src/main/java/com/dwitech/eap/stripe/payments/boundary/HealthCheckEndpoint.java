package com.dwitech.eap.stripe.payments.boundary;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;
import org.eclipse.microprofile.health.Readiness;

import javax.enterprise.context.ApplicationScoped;
import java.util.Date;

import static java.lang.System.currentTimeMillis;
import static org.eclipse.microprofile.health.HealthCheckResponse.named;

@Liveness @Readiness
@ApplicationScoped
public class HealthCheckEndpoint implements HealthCheck {

    @Override
    public HealthCheckResponse call() {
        return named("Stripe-Payments-Service Reachable: " + new Date(currentTimeMillis())).up().build();
    }
}