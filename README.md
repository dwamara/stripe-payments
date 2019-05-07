# stripe-payments

This simple application build with Quarkus and exposing a REST endpoint implements the Automatic confirmation of the Stripe Payment Intens API.

A simple json object request should be sent to the endpoint and should be as follow:
{
	"currency": "<the_currency_of_your_transaction>",
	"amount": <the_amount_of_your_transaction>
}

Following response will be sent back to the client:
