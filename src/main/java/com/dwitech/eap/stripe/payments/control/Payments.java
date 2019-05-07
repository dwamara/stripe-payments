package com.dwitech.eap.stripe.payments.control;

import com.stripe.model.PaymentIntent;

import javax.enterprise.context.ApplicationScoped;
import javax.json.JsonObject;
import javax.ws.rs.core.Response;

import static javax.json.Json.createObjectBuilder;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static javax.ws.rs.core.Response.ok;
import static javax.ws.rs.core.Response.status;

@ApplicationScoped
public class Payments {
    public Response generatePaymentResponse(final PaymentIntent intent) {
        JsonObject jsonObject;
        if (intent.getStatus().equals("requires_action") && intent.getNextAction().getType().equals("use_stripe_sdk")) {
            jsonObject = createObjectBuilder()
                    .add("requires_action", true)
                    .add("payment_intent_client_secret", intent.getClientSecret())
                    .build();
            return ok(jsonObject).build();
        } else if (intent.getStatus().equals("succeeded")) {
            jsonObject = createObjectBuilder()
                    .add("success", true)
                    .build();
            return ok(jsonObject).build();
        }
        jsonObject = createObjectBuilder()
                .add("Error", "Invalid status")
                .build();
        return status(INTERNAL_SERVER_ERROR).entity(jsonObject).build();
    }
}
