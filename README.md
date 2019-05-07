# stripe-payments

This simple application built with Quarkus and exposing a REST endpoint implements the Automatic confirmation of the Stripe Payment Intents API.

A simple json object request should be sent to the endpoint and should be as follow:
```json
{
	"currency": "<the_currency_of_your_transaction>",
	"amount": <the_amount_of_your_transaction_in_cents>
}
```

Following response will be sent back to the client:
```json
{
	"client_secret" : "<the_transaction_secret_for_the_payment_intent_to_be_returned_to_the_client>"
}
```

To be used in a live system, the value of the key
```stripe.secret.key```
should be changed to the productive secret key in the application.properties file
