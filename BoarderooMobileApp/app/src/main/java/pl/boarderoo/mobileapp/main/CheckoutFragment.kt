package pl.boarderoo.mobileapp.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.fragment.app.FragmentActivity
import com.paypal.android.corepayments.CoreConfig
import com.paypal.android.corepayments.Environment
import com.paypal.android.corepayments.PayPalSDKError
import com.paypal.android.paypalwebpayments.PayPalWebCheckoutClient
import com.paypal.android.paypalwebpayments.PayPalWebCheckoutListener
import com.paypal.android.paypalwebpayments.PayPalWebCheckoutRequest
import com.paypal.android.paypalwebpayments.PayPalWebCheckoutResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.boarderoo.mobileapp.R
import pl.boarderoo.mobileapp.datastore.AppRuntimeData
import pl.boarderoo.mobileapp.retrofit.services.OrderService
import pl.boarderoo.mobileapp.ui.theme.BoarderooMobileAppTheme
import pl.boarderoo.mobileapp.volley.PayPalVolley

class CheckoutFragment : FragmentActivity() {
    private val clientId =
        "AZ8pZw6s44qSnIantY7aDEjGZ0mG8oMwKWLTtIpzub6boKxbGSk0OFSEfw5usTA2HfHU7me4daTaw23c"
    private var intent: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val config = CoreConfig(clientId, environment = Environment.SANDBOX)
        val returnUrl = "boarderoo://paypal"
        val payPalWebCheckoutClient =
            PayPalWebCheckoutClient(this@CheckoutFragment, config, returnUrl)
        setContent {
            val context = LocalContext.current
            val activity = LocalActivity.current
            val data: Uri? = intent?.data
            if (!(data != null && data.scheme == "boarderoo" && data.host == "paypal")) {
                LaunchedEffect(Unit) {
                    val payPalWebCheckoutRequest = PayPalWebCheckoutRequest(AppRuntimeData.orderId)
                    payPalWebCheckoutClient.start(payPalWebCheckoutRequest)
                }
            }
            BoarderooMobileAppTheme {
                Surface(
                    color = colorResource(R.color.backgroundColor)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = {
                                context.startActivity(
                                    Intent(context, MainActivity::class.java)
                                )
                                activity?.finish()
                            }
                        ) {
                            Text("Back to Main Page")
                        }
                    }
                }
            }
            // This Listener does not work at all, it always calls onPayPalWebCancelled, regardless of the actual result.
            payPalWebCheckoutClient.listener = object : PayPalWebCheckoutListener {
                override fun onPayPalWebSuccess(result: PayPalWebCheckoutResult) {
                    //Do nothing

                }

                override fun onPayPalWebFailure(error: PayPalSDKError) {
                    //Do nothing
                }

                override fun onPayPalWebCanceled() {
                    //Do nothing
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        this.intent = intent
        if (intent.data!!.getQueryParameter("opType") == "payment") {
            PayPalVolley.captureOrder(
                AppRuntimeData.orderId,
                onSuccess = {
                    AppRuntimeData.order!!.paymentNumber = it
                    CoroutineScope(Dispatchers.IO).launch {
                        //TODO: fix order sending
                        val response = OrderService().addOrder(AppRuntimeData.order!!)
                        if(response.isSuccessful) {

                        } else {
                            Log.d("ORDER", response.errorBody()!!.string())
                        }
                    }
                    Toast.makeText(this@CheckoutFragment, "Payment successful", Toast.LENGTH_SHORT).show()
                },
                onError = {
                    //TODO: handle error
                }
            )
        }
    }
}