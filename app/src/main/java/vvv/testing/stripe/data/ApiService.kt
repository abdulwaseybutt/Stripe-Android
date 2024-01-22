package vvv.testing.stripe.data

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import vvv.testing.stripe.model.MakePaymentRequest
import vvv.testing.stripe.model.MakePaymentResponse

interface ApiService {

    @POST("/payment-sheet")
    fun makePayment(@Body requestBody: MakePaymentRequest): Call<MakePaymentResponse>

}
