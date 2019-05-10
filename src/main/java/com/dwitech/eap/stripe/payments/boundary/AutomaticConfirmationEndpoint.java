package com.dwitech.eap.stripe.payments.boundary;

import com.dwitech.eap.stripe.payments.entity.PaymentRequest;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;

import static com.stripe.Stripe.apiKey;
import static com.stripe.model.PaymentIntent.create;
import static com.stripe.param.PaymentIntentCreateParams.builder;
import static java.lang.Double.parseDouble;
import static javax.json.Json.createObjectBuilder;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static javax.ws.rs.core.Response.ok;
import static javax.ws.rs.core.Response.status;

@Path("/ac")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
public class AutomaticConfirmationEndpoint {
    @ConfigProperty(name = "stripe.currency") String defaultCurrency;
    @ConfigProperty(name = "stripe.secret.key") String secretKey;

    @POST
    public Response processPayment(final PaymentRequest request) {
        apiKey = secretKey;
        ResponseBuilder responseBuilder;

        try {
            final Double amountParam = request.getAmount();

            if (emptyParameter(amountParam)) {
                return status(400).entity(
                        createObjectBuilder().add("message", "transaction amount is required")
                ).build();
            }

            final PaymentIntentCreateParams createParams = builder()
                    .setAmount(new Double(amountParam).longValue())
                    .setCurrency(request.getCurrency() == null || request.getCurrency().isEmpty() ? defaultCurrency : request.getCurrency())
                    .build();

            final PaymentIntent intent = create(createParams);
            responseBuilder = ok(
                    createObjectBuilder().add("client_secret", intent.getClientSecret()
                    ).build()
            );
        } catch (StripeException sExc) {
            responseBuilder = status(INTERNAL_SERVER_ERROR).entity(
                    createObjectBuilder().add("message", sExc.getMessage()
                    ).build()
            );
        }
        return responseBuilder.build();
    }

    @GET
    public Response processPaymentWithGetRequest(@Context UriInfo info) {
        final String amountParam = info.getQueryParameters().getFirst("amount");

        if (emptyParameter(amountParam)) {
            return status(400)
                    .entity(createObjectBuilder().add("message", "transaction amount is required")
                    ).build();
        }

        final double amount = parseDouble(amountParam);

        final String currencyParam = info.getQueryParameters().getFirst("currency");
        final String currency = emptyParameter(currencyParam) ? defaultCurrency : currencyParam;
        return processPayment(new PaymentRequest(amount, currency));
    }

    private boolean emptyParameter(final String param) {
        return param == null || param.isEmpty();
    }

    private boolean emptyParameter(final Double param) {
        return param == null || param == 0;
    }
}
