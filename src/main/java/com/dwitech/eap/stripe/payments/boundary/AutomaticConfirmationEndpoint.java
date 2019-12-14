package com.dwitech.eap.stripe.payments.boundary;

import com.dwitech.eap.stripe.payments.control.Payments;
import com.dwitech.eap.stripe.payments.entity.CreatePaymentRequest;
import com.dwitech.eap.stripe.payments.entity.UpdatePaymentRequest;
import io.quarkus.arc.config.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import static com.dwitech.eap.stripe.payments.control.Validator.emptyParameter;
import static javax.json.Json.createObjectBuilder;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.status;

@Path("/ac") @Produces(APPLICATION_JSON)
@ConfigProperties(prefix = "stripe")
public class AutomaticConfirmationEndpoint {
    @ConfigProperty(name = "currency") String defaultCurrency;
    @ConfigProperty(name = "secret.key") String secretKey;
    @Inject Payments payments;

    @POST @Path("/create")
    public Response confirmPaymentFromHttpPostRequest(final CreatePaymentRequest request) {
        return payments.confirmPayment(request);
    }

    @GET @Path("/create")
    public Response confirmPaymentFromHttpGetRequest(@QueryParam("amount") final double amount, @QueryParam("currency") String currency) {
        try {
            if (emptyParameter(amount)) {
                return status(BAD_REQUEST)
                        .entity(createObjectBuilder().add("message", "transaction amount is required")
                        ).build();
            }

            currency = emptyParameter(currency) ? defaultCurrency : currency;
            return payments.confirmPayment(new CreatePaymentRequest(amount, currency));
        } catch (NumberFormatException nfExc) {
            return status(BAD_REQUEST)
                    .entity(createObjectBuilder().add("message", "transaction amount was invalid")
                    ).build();
        }
    }

    @POST @Path("/update")
    public Response updatePaymentFromHttpPostRequest(UpdatePaymentRequest request) {
        return payments.updatePayment(request);
    }

    @GET @Path("/update")
    public Response updatePaymentFromHttpGetRequest(@QueryParam("amount") final double amount, @QueryParam("currency") String currency, @QueryParam("payId") String paymentIntentId) {
        try {
            if (emptyParameter(amount)) {
                return status(BAD_REQUEST)
                               .entity(createObjectBuilder().add("message", "transaction amount is required")
                               ).build();
            }

            if (emptyParameter(paymentIntentId)) {
                return status(BAD_REQUEST)
                               .entity(createObjectBuilder().add("message", "paymentIntentId is required")
                               ).build();
            }

            currency = emptyParameter(currency) ? defaultCurrency : currency;
            return payments.updatePayment(new UpdatePaymentRequest(amount, currency, paymentIntentId));
        } catch (NumberFormatException nfExc) {
            return status(BAD_REQUEST)
                           .entity(createObjectBuilder().add("message", "transaction amount was invalid")
                           ).build();
        }
    }
}