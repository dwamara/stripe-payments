package com.dwitech.eap.stripe.payments.boundary.health;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

import javax.enterprise.context.ApplicationScoped;
import java.util.Date;

import static java.lang.System.currentTimeMillis;
import static org.eclipse.microprofile.health.HealthCheckResponse.named;

@ApplicationScoped @Liveness
public class LivenessCheck implements HealthCheck {
    @Override
    public HealthCheckResponse call() {
        return named("Stripe-Payments-Service Reachable : " + new Date(currentTimeMillis()))
                .up()
                .build();
    }
}