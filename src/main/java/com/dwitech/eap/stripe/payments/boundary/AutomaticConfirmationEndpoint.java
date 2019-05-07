package com.dwitech.eap.stripe.payments.boundary;

import com.dwitech.eap.stripe.payments.control.Payments;
import com.dwitech.eap.stripe.payments.entity.PaymentRequest;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import static com.stripe.Stripe.apiKey;
import static com.stripe.model.PaymentIntent.create;
import static com.stripe.param.PaymentIntentCreateParams.builder;
import static javax.json.Json.createObjectBuilder;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static javax.ws.rs.core.Response.ok;
import static javax.ws.rs.core.Response.status;

@Path("/ac")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
public class AutomaticConfirmationEndpoint {
    @Inject @ConfigProperty(name = "stripe.currency") String currency;
    @Inject @ConfigProperty(name = "stripe.secret.key") String secretKey;
    @Inject
    Payments payments;

    @POST @Path("/process")
    public Response processPayment(final PaymentRequest request) {
        apiKey = secretKey;
        ResponseBuilder responseBuilder;

        try {
            final PaymentIntentCreateParams createParams = builder()
                    .setAmount((long) request.getAmount())
                    .setCurrency(request.getCurrency())
                    .build();

            final PaymentIntent intent = create(createParams);
            responseBuilder = ok(
                    createObjectBuilder().add("client_secret", intent.getClientSecret()).build()
            );
        } catch (StripeException sExc) {
            responseBuilder = status(INTERNAL_SERVER_ERROR).entity(
                    createObjectBuilder().add("message", sExc.getMessage()).build()
            );
        }
        return responseBuilder.build();
    }

    @GET @Path("/process")
    public Response processPaymentWithGetRequest(@QueryParam("amount") final double amount, @QueryParam("currency") final String currency) {
        return processPayment(new PaymentRequest(amount, currency));
    }
}