package pl.boarderoo.mobileapp.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.runtime.rememberCoroutineScope
import androidx.fragment.app.FragmentActivity
import com.paypal.android.corepayments.CoreConfig
import com.paypal.android.corepayments.Environment
import com.paypal.android.corepayments.PayPalSDKError
import com.paypal.android.paypalwebpayments.PayPalWebCheckoutClient
import com.paypal.android.paypalwebpayments.PayPalWebCheckoutListener
import com.paypal.android.paypalwebpayments.PayPalWebCheckoutResult

class CheckoutFragment : FragmentActivity() {
    private val clientId = "AZ8pZw6s44qSnIantY7aDEjGZ0mG8oMwKWLTtIpzub6boKxbGSk0OFSEfw5usTA2HfHU7me4daTaw23c"
    private var intent: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val config = CoreConfig(clientId, environment = Environment.SANDBOX)
        val returnUrl = "boarderoo://paypal"
        val payPalWebCheckoutClient =
            PayPalWebCheckoutClient(this@CheckoutFragment, config, returnUrl)
        setContent {
            val scope = rememberCoroutineScope()
            val data: Uri? = intent?.data
            if(!(data != null && data.scheme == "boarderoo" && data.host == "paypal")) {
                //TODO: create order on fragment launch
            }
            // Set the PayPalWebCheckoutListener to handle success, failure, or cancellation
            payPalWebCheckoutClient.listener = object : PayPalWebCheckoutListener {
                override fun onPayPalWebSuccess(result: PayPalWebCheckoutResult) {
                    //TODO: capture order and add order to database
                }

                override fun onPayPalWebFailure(error: PayPalSDKError) {
                    Toast.makeText(this@CheckoutFragment, "Payment Failed: ${error.message}", Toast.LENGTH_SHORT).show()
                }

                override fun onPayPalWebCanceled() {
                    Toast.makeText(this@CheckoutFragment, "Payment Canceled", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        this.intent = intent
    }
}