package vvv.testing.stripe.model

data class MakePaymentResponse(
    val paymentIntent: String,
    val ephemeralKey: String,
    val customer: String,
    val publishableKey: String,
)