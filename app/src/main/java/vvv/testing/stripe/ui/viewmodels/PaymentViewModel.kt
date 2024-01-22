package vvv.testing.stripe.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.stripe.android.paymentsheet.PaymentSheet
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vvv.testing.stripe.data.ApiService
import vvv.testing.stripe.data.RetrofitClient
import vvv.testing.stripe.model.MakePaymentRequest
import vvv.testing.stripe.model.MakePaymentResponse
import java.util.Calendar

class PaymentViewModel : ViewModel() {
    private val apiService = RetrofitClient.getClient().create(ApiService::class.java)
    private lateinit var paymentIntentClientSecret: String
    private lateinit var configuration: PaymentSheet.CustomerConfiguration

    fun makePayment(param: String) {
        val call: Call<MakePaymentResponse> = apiService.makePayment(MakePaymentRequest(param))
        call.enqueue(object : Callback<MakePaymentResponse> {
            override fun onResponse(
                call: Call<MakePaymentResponse>, response: Response<MakePaymentResponse>
            ) {
                if (response.isSuccessful) {
                    val resp = response.body()
                    if (resp != null) {
                        configuration =
                            PaymentSheet.CustomerConfiguration(resp.customer, resp.ephemeralKey)
                        paymentIntentClientSecret = resp.paymentIntent
                    } else {
                        // Handle null response
                    }
                } else {

                }
            }

            override fun onFailure(call: Call<MakePaymentResponse>, t: Throwable) {
                // Handle network errors or other failures here
            }
        })
    }
}

class PaymentViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PaymentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return PaymentViewModel(
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
