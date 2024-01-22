package vvv.testing.stripe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vvv.testing.stripe.data.ApiService
import vvv.testing.stripe.data.RetrofitClient
import vvv.testing.stripe.model.MakePaymentRequest
import vvv.testing.stripe.model.MakePaymentResponse
import vvv.testing.stripe.ui.viewmodels.PaymentViewModel
import vvv.testing.stripe.ui.viewmodels.PaymentViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var paymentSheet: PaymentSheet
    private lateinit var viewModel: PaymentViewModel

    private val apiService = RetrofitClient.getClient().create(ApiService::class.java)
    private lateinit var paymentIntentClientSecret: String
    private lateinit var configuration: PaymentSheet.CustomerConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val viewModelFactory = PaymentViewModelFactory()
//        viewModel = ViewModelProvider(this, viewModelFactory)[PaymentViewModel::class.java]

        paymentSheet = PaymentSheet(this, this::onPaymentSheetResult)

        findViewById<Button>(R.id.paymentBtn).setOnClickListener {
//            viewModel.makePayment("abc")
            makePayment("abc")

        }
    }

    fun makePayment(authKey: String) {
        val call: Call<MakePaymentResponse> = apiService.makePayment(MakePaymentRequest(authKey))
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
                        PaymentConfiguration.init(applicationContext, resp.publishableKey)

                        paymentSheet.presentWithPaymentIntent(
                            paymentIntentClientSecret,
                            PaymentSheet.Configuration("Merchant Name", configuration)
                        )
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

    private fun onPaymentSheetResult(result: PaymentSheetResult) {
        when (result) {
            is PaymentSheetResult.Canceled -> {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()

            }

            is PaymentSheetResult.Failed -> {
                Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show()

            }

            is PaymentSheetResult.Completed -> {
                Toast.makeText(this, "Payment Success!", Toast.LENGTH_LONG).show()

            }
        }

    }
}