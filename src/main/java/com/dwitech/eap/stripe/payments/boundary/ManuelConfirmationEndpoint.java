package com.dwitech.eap.stripe.payments.boundary;

import com.dwitech.eap.stripe.payments.control.Payments;
import com.dwitech.eap.stripe.payments.entity.ConfirmPaymentRequest;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import static com.stripe.Stripe.apiKey;
import static com.stripe.model.PaymentIntent.create;
import static com.stripe.model.PaymentIntent.retrieve;
import static com.stripe.param.PaymentIntentCreateParams.ConfirmationMethod.MANUAL;
import static com.stripe.param.PaymentIntentCreateParams.builder;
import static java.lang.Long.valueOf;
import static javax.json.Json.createObjectBuilder;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static javax.ws.rs.core.Response.status;

@Path("/mc") @Produces(APPLICATION_JSON)
public class ManuelConfirmationEndpoint {
    @ConfigProperty(name = "stripe.currency") String currency;
    @ConfigProperty(name = "stripe.secret.key") String secretKey;
    @Inject Payments payments;

    @POST @Path("/confirm")
    public Response confirmPayment(final ConfirmPaymentRequest request) {
        apiKey = secretKey;
        PaymentIntent intent = null;
        try {
            if (request.getPaymentMethodId() != null) {
                final PaymentIntentCreateParams createParams = builder()
                        .setAmount(valueOf(request.getPaymentAmount()))
                        .setCurrency(request.getCurrency() == null || request.getCurrency().isEmpty() ? currency : request.getCurrency())
                        .setConfirm(true)
                        .setPaymentMethod(request.getPaymentMethodId())
                        .setConfirmationMethod(MANUAL)
                        .build();
                intent = create(createParams);
            } else if (request.getPaymentIntentId() != null) {
                intent = retrieve(request.getPaymentIntentId());
                intent.confirm();
            }
            return payments.generatePaymentResponse(intent);
        } catch (Exception exc) {
            return status(INTERNAL_SERVER_ERROR).entity(
                    createObjectBuilder().add("message", exc.getMessage()).build()
            ).build();
        }
    }
}