package pl.boarderoo.mobileapp.start

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import pl.boarderoo.mobileapp.DarkButton
import pl.boarderoo.mobileapp.R
import pl.boarderoo.mobileapp.ui.theme.BoarderooMobileAppTheme


class StartActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val animationSpeed = 400

        setContent {
            BoarderooMobileAppTheme {
                Surface(
                    color = colorResource(R.color.backgroundColor)
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "LogoScreen",
                        enterTransition = {
                            slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(animationSpeed)
                            )
                        },
                        exitTransition = {
                            slideOutOfContainer(
                                AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(animationSpeed)
                            )
                        },
                        popEnterTransition = {
                            slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.Right,
                                animationSpec = tween(animationSpeed)
                            )
                        },
                        popExitTransition = {
                            slideOutOfContainer(
                                AnimatedContentTransitionScope.SlideDirection.Right,
                                animationSpec = tween(animationSpeed)
                            )
                        }
                    ) {
                        composable(route = "LogoScreen") { LogoScreen(navController) }
                        composable(route = "LoginScreen/{email}") { backStackEntry ->
                            val email = backStackEntry.arguments?.getString("email") ?: ""
                            LoginScreen(navController, email)
                        }
                        composable(route = "RegisterScreen/{email}") { backStackEntry ->
                            val email = backStackEntry.arguments?.getString("email") ?: ""
                            RegisterScreen(navController, email)
                        }
                        composable(route = "WebViewScreen") { WebViewScreen() }
                    }

                    val data: Uri? = intent.data

                    if (data != null && data.scheme == "boarderoo" && data.host == "callback") {
                        // Aplikacja została uruchomiona z callbacka
                        val code = data.getQueryParameter("code")
                        println("Code: $code")

                        /*
                        *
                        * TUTAJ BĘDZIE ZAPYTANIE API DO BACKENDU CZY KONTO ISTNIEJE I JEŻELI TAK TO LOGOWANIE
                        * A JEŻELI NIE TO REJESTRACJA (OBA Z PRZEKAZANIEM PARAMETRU)
                        *
                        * */
                        val exist = false
                        val email = "test@gmail.com"
                        if(exist) {
                            navController.navigate("LoginScreen/$email")
                        }
                        else{
                            navController.navigate("RegisterScreen/$email")
                        }

                    } else {
                        // Aplikacja została uruchomiona w tradycyjny sposób
                        println("Aplikacja została uruchomiona standardowo")
                    }
                }
            }
        }
    }
}

@Composable
fun LogoScreen(navController: NavController) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val elementWidth = (screenWidth * 0.60).dp
    val buttonWidth = (screenWidth * 0.70).dp
    val buttonHeight = elementWidth / 4
    val fontSize = (buttonHeight.value * 0.4).sp
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
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
        Spacer(modifier = Modifier.fillMaxHeight(0.05f)) // Przerwa między logo a przyciskami
        DarkButton(
            text = "Logowanie",
            buttonWidth = buttonWidth,
            buttonHeight = buttonHeight,
            fontSize = fontSize,
            onClick = {
                navController.navigate("LoginScreen")
            }
        )
        Spacer(modifier = Modifier.fillMaxHeight(0.03f)) // Przerwa między logo a przyciskami
        DarkButton(
            text = "Rejestracja",
            buttonWidth = buttonWidth,
            buttonHeight = buttonHeight,
            fontSize = fontSize,
            onClick = {
                navController.navigate("RegisterScreen")
            }
        )
        Spacer(modifier = Modifier.fillMaxHeight(0.03f)) // Przerwa między logo a przyciskami
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(id = R.drawable.google_logo),
                contentDescription = "Google Icon",
                modifier = Modifier
                    .size(buttonHeight) // Użyj obliczonego rozmiar
                    .clip(RoundedCornerShape(buttonHeight / 2))
                    .clickable {
                        // Obsługa kliknięcia
                    }
            )
            Spacer(modifier = Modifier.fillMaxWidth(0.03f)) // Przerwa między logo a przyciskami
            Image(
                painter = painterResource(id = R.drawable.discord),
                contentDescription = "Discord Icon",
                modifier = Modifier
                    .size(buttonHeight) // Użyj obliczonego rozmiaru
                    .border(
                        BorderStroke(2.dp, Color(0xFF5865F2)),
                        shape = RoundedCornerShape(buttonHeight / 2)
                    )
                    .clip(RoundedCornerShape(buttonHeight / 2))
                    .clickable {
                        navController.navigate("WebViewScreen")  // Nawigacja do WebViewScreen
                    }
            )
        }
    }
}

@Composable
fun WebViewScreen() {

    val context = LocalContext.current
     // Custom Tabs do otwarcia Discord OAuth2
    val targetUrl = "https://discord.com/oauth2/authorize?client_id=1303087880503296182&response_type=code&redirect_uri=http%3A%2F%2F192.168.1.13%2Fcallback&scope=email"

    val customTabsIntent = CustomTabsIntent.Builder()
        .setShowTitle(true)
        .build()

    customTabsIntent.launchUrl(context, Uri.parse(targetUrl))
}

@Preview(showBackground = true)
@Composable
fun PreviewLogoScreen() {
    LogoScreen(rememberNavController())
}

@Composable
fun LoginScreen() {
    val navController = rememberNavController()
    navController.navigate("LoginScreen")
}

