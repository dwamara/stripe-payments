package com.dwitech.eap.stripe.payments.boundary;

import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.ws.rs.*;
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

@Path("/charges")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
@Tag(name = "Charges service", description = "Return the complete list of attempted charges on a payment intent")
public class ChargesEndpoint {
    @ConfigProperty(name = "stripe.secret.key") String secretKey;

    @GET @Path("/{payment_intent}")
    @Operation(description = "Start the process of a particular payment request")
    @APIResponse(responseCode = "200", description = "Successful, returning the client secret for this payment request")
    @APIResponse(responseCode = "400", description = "Error, the transaction amount was 0, missing from the request or was not in a valid format")
    @APIResponse(responseCode = "500", description = "Error, there was a problem notified by the Stripe service")
    public Response generateClientSecretFromHttpGetRequest(@PathParam("payment_intent") final String paymentIntent) {
        apiKey = secretKey;
        ResponseBuilder responseBuilder;

        try {
            if (paymentIntent == null || paymentIntent.isEmpty()) {
                return status(BAD_REQUEST)
                        .entity(createObjectBuilder().add("message", "client_secret amount is required")
                        ).build();
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