package pl.boarderoo.mobileapp.start

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import pl.boarderoo.mobileapp.R
import pl.boarderoo.mobileapp.datastore.AppRuntimeData
import pl.boarderoo.mobileapp.datastore.getLoginState
import pl.boarderoo.mobileapp.datastore.getUserEmail
import pl.boarderoo.mobileapp.main.MainActivity
import pl.boarderoo.mobileapp.retrofit.services.UserService
import pl.boarderoo.mobileapp.ui.theme.BoarderooMobileAppTheme

class LoadingScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val userService = UserService()
            val context = LocalContext.current
            val isLoggedIn by getLoginState(context).collectAsState(initial = false)
            val email by getUserEmail(context).collectAsState(initial = "")
            Log.d("DATASTORE", "isLoggedIn: $isLoggedIn")
            Log.d("DATASTORE", "email: $email")
            LaunchedEffect(isLoggedIn) {
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
                            painter = painterResource(R.drawable.logo),
                            contentDescription = "Logo"
                        )
                    }
                }
            }
        }
    }
}