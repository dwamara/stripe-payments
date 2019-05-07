# stripe-payments

This simple application built with Quarkus and exposing a REST endpoint implements the Automatic confirmation of the Stripe Payment Intents API.

The endpoint can be called either with a POST or a GET request passing the currency and the amount of the transaction. In case the currency is not provided, a default currency will be taken from the ```stripe.currency``` value set in the application properties file. 

For both the POST and the GET methods, following URL should be called: ```http://<server>:<port>/ac/process```

For the POST method, a simple json object request should be sent to the endpoint and should be as follow:
```json
{
	"currency": "<the_currency_of_your_transaction>",
	"amount": <the_amount_of_your_transaction_in_cents>
}
```

For the GET method, following URL has to be called: ```http://<server>:<port>/ac/process?currency=<the_currency_of_your_transaction>&amount=<the_amount_of_your_transaction_in_cents>```

For both methods, following response will be returned:
```json
{
	"client_secret" : "<the_transaction_secret_for_the_payment_intent_to_be_returned_to_the_client>"
}
```

To be used in a live system, the value of the key ```stripe.secret.key``` should be changed to the productive secret key in the application.properties file
