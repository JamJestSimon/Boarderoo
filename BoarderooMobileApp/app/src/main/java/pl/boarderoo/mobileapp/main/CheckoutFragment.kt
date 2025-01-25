package pl.boarderoo.mobileapp.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import pl.boarderoo.mobileapp.DarkButton
import pl.boarderoo.mobileapp.R
import pl.boarderoo.mobileapp.datastore.AppRuntimeData
import pl.boarderoo.mobileapp.retrofit.services.OrderService
import pl.boarderoo.mobileapp.ui.theme.BoarderooMobileAppTheme
import pl.boarderoo.mobileapp.volley.PayPalVolley

class CheckoutFragment : FragmentActivity() {
    private val clientId =
        "AZ8pZw6s44qSnIantY7aDEjGZ0mG8oMwKWLTtIpzub6boKxbGSk0OFSEfw5usTA2HfHU7me4daTaw23c"
    private var intent: Intent? = null
    private var isLoading by mutableStateOf(true) // Stan ładowania
    private var paymentStatusMessage by mutableStateOf("")

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
                    color = colorResource(R.color.backgroundColor),
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Layout z kręciołkiem i treścią
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        // Kręciołek (wskazanie ładowania)
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(80.dp), // Ustawienie rozmiaru
                                color = colorResource(id = R.color.buttonColor) // Kolor
                            )
                        } else {
                            // Logo (Image)
                            Image(
                                painter = painterResource(R.drawable.logo),
                                contentDescription = "Logo",
                                modifier = Modifier
                                    .fillMaxWidth(0.6f)
                                    .padding(10.dp)
                                    .border(
                                        BorderStroke(5.dp, colorResource(id = R.color.buttonColor)),
                                        shape = RoundedCornerShape(25.dp)
                                    )
                                    .clip(RoundedCornerShape(25.dp))
                            )

                            // Tekst zależny od statusu płatności
                            Text(
                                text = paymentStatusMessage,
                                modifier = Modifier.padding(bottom = 16.dp),
                                style = TextStyle(
                                    fontSize = 20.sp, // Ustawienie większego rozmiaru czcionki
                                    color = Color(0xFF422D29), // Ustawienie koloru #422D29
                                    fontWeight = FontWeight.Bold // Pogrubienie
                                )
                            )

                            DarkButton(
                                text = "Wróć do strony głównej!",
                                buttonWidth = 20.dp + LocalConfiguration.current.screenWidthDp.dp,
                                buttonHeight = 60.dp,
                                fontSize = 20.sp,
                                onClick = {
                                    context.startActivity(
                                        Intent(context, MainActivity::class.java)
                                    )
                                    activity?.finish()
                                }
                            )

                        }
                    }
                }
            }
        }

        // Listener na odpowiedź z PayPal (zmiana stanu ładowania)
        payPalWebCheckoutClient.listener = object : PayPalWebCheckoutListener {
            override fun onPayPalWebSuccess(result: PayPalWebCheckoutResult) {
                // Ustawienie stanu na false, aby pokazać zawartość
                isLoading = false
                paymentStatusMessage = "Płatność zakończona sukcesem!"
            }

            override fun onPayPalWebFailure(error: PayPalSDKError) {
                // Ustawienie stanu na false, aby pokazać zawartość
                isLoading = false
                paymentStatusMessage = "Wystąpił błąd podczas płatności."
            }

            override fun onPayPalWebCanceled() {
                // Ustawienie stanu na false, aby pokazać zawartość
                isLoading = false
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
                        val response = OrderService().addOrder(AppRuntimeData.order!!)
                        if (response.isSuccessful) {
                            // Success - update state
                            isLoading = false
                            paymentStatusMessage = "Płatność zakończona sukcesem!"
                        } else {
                            Log.d("ORDER", response.errorBody()!!.string())
                            paymentStatusMessage = "Wystąpił błąd przy zapisie zamówienia."
                            isLoading = false
                        }
                    }
                },
                onError = {
                    // Handle error
                    isLoading = false
                    paymentStatusMessage = "Wystąpił problem podczas płatności."
                }
            )
        }
    }
}
