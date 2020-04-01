package com.dwitech.eap.stripe.payments.boundary;

import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import java.util.List;

import static com.stripe.Stripe.apiKey;
import static com.stripe.model.PaymentIntent.retrieve;
import static javax.json.Json.createObjectBuilder;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static javax.ws.rs.core.Response.ok;
import static javax.ws.rs.core.Response.status;

@Path("/charges") @Produces(APPLICATION_JSON)
public class ChargesEndpoint {
    @ConfigProperty(name = "stripe.secret.key") String secretKey;

    @GET @Path("/{payment_intent}")
    public Response generateClientSecretFromHttpGetRequest(@PathParam("payment_intent") final String paymentIntent) {
        apiKey = secretKey;
        ResponseBuilder responseBuilder;

        try {
            if (paymentIntent == null || paymentIntent.isEmpty()) {
                return status(BAD_REQUEST)
                        .entity(createObjectBuilder().add("message", "client_secret amount is required"))
                               .build();
            }

            responseBuilder =  ok(new GenericEntity<List<Charge>>(retrieve(paymentIntent).getCharges().getData()) {});
        } catch (NumberFormatException nfExc) {
            responseBuilder = status(BAD_REQUEST)
                    .entity(createObjectBuilder().add("message", "transaction amount was invalid")
                    );
        } catch (StripeException sExc) {
            responseBuilder = status(INTERNAL_SERVER_ERROR).entity(
                    createObjectBuilder().add("message", sExc.getMessage()
                    ).build()
            );
        }
        return responseBuilder.build();
    }
}