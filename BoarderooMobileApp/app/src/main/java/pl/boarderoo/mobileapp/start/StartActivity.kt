package pl.boarderoo.mobileapp.start

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
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
                        composable(route = "LoginScreen") { LoginScreen(navController) }
                        composable(route = "RegisterScreen/{email}") { backStackEntry ->
                            val email = backStackEntry.arguments?.getString("email") ?: ""
                            RegisterScreen(navController, email)
                        }
                    }

                    val data: Uri? = intent.data

                    if (data != null && data.scheme == "boarderoo" && data.host == "callback") {
                        // Aplikacja została uruchomiona z callbacka
                        val code = data.getQueryParameter("code")
                        println("Code: $code")

                        /*
                        *
                        * TUTAJ BĘDZIE ZAPYTANIE API DO BACKENDU CZY KONTO ISTNIEJE I JEŻELI TAK TO LOGOWANIE auto
                        * A JEŻELI NIE TO REJESTRACJA (OBA Z PRZEKAZANIEM PARAMETRU)
                        *
                        * */
                        val exist = false
                        val email = "test@gmail.com"
                        if(exist) {
                            //LOGOWANIE
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

    val discordUrl = stringResource(R.string.discord_redirect)
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
                navController.navigate("RegisterScreen/")
            }
        )
        Spacer(modifier = Modifier.fillMaxHeight(0.03f)) // Przerwa między logo a przyciskami
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {

            val context = LocalContext.current

            val googleSignInClient = remember {
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken("928336702407-qnh2oanp2oofcliefcc1v24355lc1nan.apps.googleusercontent.com")
                    .requestEmail()
                    .build()
                GoogleSignIn.getClient(context, gso)
            }

            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartActivityForResult()
            ) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val account = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    println(account.result.email)
                    // Sprawdź, czy konto zostało poprawnie pobrane
                } else {
                    // Obsługuje niepowodzenie logowania
                    println("NNNNNNNNNNOKKK")
                    println(result)
                }
            }

            Image(
                painter = painterResource(id = R.drawable.google_logo),
                contentDescription = "Google Icon",
                modifier = Modifier
                    .size(buttonHeight) // Użyj obliczonego rozmiar
                    .clip(RoundedCornerShape(buttonHeight / 2))
                    .clickable {
                        val signInIntent = googleSignInClient.signInIntent
                        launcher.launch(signInIntent)
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
                        val targetUrl = discordUrl

                        val customTabsIntent = CustomTabsIntent.Builder()
                            .setShowTitle(true)
                            .build()

                        customTabsIntent.launchUrl(context, Uri.parse(targetUrl))
                    }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLogoScreen() {
    LogoScreen(rememberNavController())
}

