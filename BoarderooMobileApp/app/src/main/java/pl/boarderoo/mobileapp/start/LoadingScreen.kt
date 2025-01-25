package pl.boarderoo.mobileapp.start

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.first
import pl.boarderoo.mobileapp.R
import pl.boarderoo.mobileapp.datastore.AppRuntimeData
import pl.boarderoo.mobileapp.datastore.getLoginState
import pl.boarderoo.mobileapp.datastore.getUserEmail
import pl.boarderoo.mobileapp.main.MainActivity
import pl.boarderoo.mobileapp.retrofit.services.UserService
import pl.boarderoo.mobileapp.ui.theme.BoarderooMobileAppTheme
import pl.boarderoo.mobileapp.volley.PayPalVolley

class LoadingScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                PayPalVolley.createRequestQueue(context)
                val userService = UserService()
                userService.getUserByEmail("a") //wake-up call for Cloud Run Instance, to protect from errors
                val isLoggedIn = getLoginState(context).first()
                val email = getUserEmail(context).first()
                Log.d("DATASTORE", "isLoggedIn: $isLoggedIn")
                Log.d("DATASTORE", "email: $email")
                if (isLoggedIn) {
                    AppRuntimeData.user = email?.let { userService.getUserByEmail(it).body()?.data }
                    context.startActivity(
                        Intent(
                            context,
                            MainActivity::class.java
                        )
                    )
                    (context as Activity).finish()
                } else {
                    context.startActivity(
                        Intent(
                            context,
                            StartActivity::class.java
                        )
                    )
                    (context as Activity).finish()
                }
            }
            BoarderooMobileAppTheme {
                Surface(
                    color = colorResource(R.color.backgroundColor)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = "Logo",
                            modifier = Modifier
                                .fillMaxWidth(0.6f) // Użyj obliczonego rozmiaru
                                .padding(10.dp)
                                .border(
                                    BorderStroke(5.dp, colorResource(id = R.color.buttonColor)),
                                    shape = RoundedCornerShape(25.dp)
                                )
                                .clip(RoundedCornerShape(25.dp))
                        )
                    }
                }
            }
        }
    }
}