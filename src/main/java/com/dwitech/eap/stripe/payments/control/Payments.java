package com.dwitech.eap.stripe.payments.control;

import com.dwitech.eap.stripe.payments.entity.CreatePaymentRequest;
import com.dwitech.eap.stripe.payments.entity.UpdatePaymentRequest;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.json.JsonObject;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import java.util.HashMap;
import java.util.Map;

import static com.dwitech.eap.stripe.payments.control.Validator.emptyParameter;
import static com.stripe.Stripe.apiKey;
import static com.stripe.model.PaymentIntent.create;
import static com.stripe.model.PaymentIntent.retrieve;
import static com.stripe.param.PaymentIntentCreateParams.builder;
import static javax.json.Json.createObjectBuilder;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static javax.ws.rs.core.Response.ok;
import static javax.ws.rs.core.Response.status;

@ApplicationScoped
public class Payments {
    @ConfigProperty(name = "stripe.currency") String defaultCurrency;
    @ConfigProperty(name = "stripe.secret.key") String secretKey;

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

    public Response confirmPayment(final CreatePaymentRequest request) {
        apiKey = secretKey;
        ResponseBuilder responseBuilder;

        try {
            if (emptyParameter(request.getAmount())) {
                return status(BAD_REQUEST).entity(
                        createObjectBuilder().add("message", "transaction amount is required")
                ).build();
            }

            final PaymentIntentCreateParams createParams = builder()
                                                                   .setAmount((long) request.getAmount())
                                                                   .setCurrency(request.getCurrency() == null || request.getCurrency().isEmpty() ? defaultCurrency : request.getCurrency())
                                                                   .build();

            responseBuilder = ok(
                    createObjectBuilder()
                            .add("client_secret", create(createParams).getClientSecret())
                            .build()
            );
        } catch (NumberFormatException nfExc) {
            return status(BAD_REQUEST)
                           .entity(createObjectBuilder().add("message", "transaction amount was invalid")
                           ).build();
        } catch (StripeException sExc) {
            responseBuilder = status(INTERNAL_SERVER_ERROR).entity(
                    createObjectBuilder().add("message", sExc.getMessage()
                    ).build()
            );
        }
        return responseBuilder.build();
    }

    public Response updatePayment(UpdatePaymentRequest request) {
        apiKey = secretKey;
        ResponseBuilder responseBuilder;

        try {
            PaymentIntent intent = retrieve(request.getPayId());
            Map<String, Object> params = new HashMap<>();
            params.put("amount", request.getAmount());
            intent.update(params);

            responseBuilder = ok(
                    createObjectBuilder()
                            .add("client_secret", intent.getClientSecret())
                            .build()
            );
        } catch (NumberFormatException nfExc) {
            return status(BAD_REQUEST)
                           .entity(createObjectBuilder().add("message", "transaction amount was invalid")
                           ).build();
        } catch (StripeException sExc) {
            responseBuilder = status(INTERNAL_SERVER_ERROR).entity(
                    createObjectBuilder().add("message", sExc.getMessage()
                    ).build()
            );
        }
        return responseBuilder.build();
    }
}