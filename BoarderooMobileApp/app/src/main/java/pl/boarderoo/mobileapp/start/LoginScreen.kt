package pl.boarderoo.mobileapp.start

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import kotlinx.coroutines.launch
import pl.boarderoo.mobileapp.DarkButton
import pl.boarderoo.mobileapp.DialogBox
import pl.boarderoo.mobileapp.LightButton
import pl.boarderoo.mobileapp.LightTextField
import pl.boarderoo.mobileapp.PageWithTitle
import pl.boarderoo.mobileapp.R
import pl.boarderoo.mobileapp.datastore.saveLoginData
import pl.boarderoo.mobileapp.main.AppRuntimeData
import pl.boarderoo.mobileapp.main.MainActivity
import pl.boarderoo.mobileapp.retrofit.services.LoginService
import pl.boarderoo.mobileapp.retrofit.services.UserService
import pl.boarderoo.mobileapp.retrofit.models.ResponseModel

@Composable
fun LoginScreen(navController: NavController) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp

    val loginService = LoginService()
    val userService = UserService()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    val elementWidth = (screenWidth * 0.60).dp
    val elementHeight = elementWidth / 4
    val logoHeight = (screenWidth * 0.3).dp
    val forgotPasswordButtonWidth = elementWidth / 5 * 4
    val forgotPasswordButtonHeight = elementHeight / 3 * 2
    val textWidth = (screenWidth * 0.70).dp
    val textHeight = elementWidth / 4
    val fontSize = (textHeight.value * 0.4).sp
    var showDialog by remember { mutableStateOf(false) }
    var loginError by remember { mutableStateOf(false) }
    var loginErrorCode by remember { mutableStateOf(0) }
    var loginErrorMessage by remember { mutableStateOf("") }

    val gson = Gson()
    BackHandler(
        enabled = true
    ) {
        navController.navigateUp()
    }
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding)
        ) {
            PageWithTitle(
                text = "Logowanie",
                screenWidth = screenWidth,
                logoHeight = logoHeight,
                fontSize = fontSize * 1.5
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally, // Wyśrodkowanie kolumny
                    modifier = Modifier.fillMaxWidth() // Ustawia szerokość kolumny na pełną szerokość
                ) {
                    var mail by remember { mutableStateOf("") }
                    var isEmailValid by remember { mutableStateOf(true) }
                    LightTextField(
                        placeholder = "Mail",
                        value = mail,
                        onValueChange = {
                            mail = it
                            isEmailValid = it.contains("@") // Sprawdzenie, czy email zawiera '@'
                        },
                        isError = !isEmailValid, // Ustawienie stanu błędu
                        textWidth = textWidth,
                        textHeight = textHeight
                    )
                    Spacer(modifier = Modifier.height((screenWidth * 0.03).dp)) // Przerwa między logo a przyciskami
                    var password by remember { mutableStateOf("") }
                    LightTextField(
                        placeholder = "Hasło",
                        value = password,
                        visualTransformation = PasswordVisualTransformation(),
                        onValueChange = {
                            password = it
                        },
                        isError = false,
                        textWidth = textWidth,
                        textHeight = textHeight
                    )
                    Spacer(modifier = Modifier.height((screenWidth * 0.05).dp)) // Przerwa między logo a przyciskami
                    DarkButton(
                        text = "Zaloguj",
                        fontSize = fontSize,
                        buttonWidth = elementWidth,
                        buttonHeight = elementHeight,
                        onClick = {
                            if (!isEmailValid || mail == "" || password == "") {
                                scope.launch {
                                    snackbarHostState
                                        .showSnackbar(
                                            message = "Podaj brakujące dane logowania"
                                        )
                                }
                            } else {
                                scope.launch {
                                    var response = loginService.getLoginResult(mail, password)
                                    if (response.code() == 500) {
                                        response = loginService.getLoginResult(mail, password)
                                    }
                                    if (response.code() == 200) {
                                        saveLoginData(context, true, mail)
                                        AppRuntimeData.user = userService.getUserByEmail(mail).body()!!.data
                                        context.startActivity(
                                            Intent(
                                                context,
                                                MainActivity::class.java
                                            )
                                        )
                                        (context as Activity).finish()
                                    } else {
                                        loginErrorCode = response.code()
                                        loginErrorMessage = gson.fromJson(response.errorBody()!!.string(), ResponseModel::class.java).message
                                        loginError = true
                                    }
                                }
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height((screenWidth * 0.03).dp)) // Przerwa między przyciskami
                    LightButton(
                        text = "Zapomniałeś hasła?",
                        fontSize = fontSize / 2,
                        buttonWidth = forgotPasswordButtonWidth,
                        buttonHeight = forgotPasswordButtonHeight,
                        onClick = {
                            showDialog = true
                        }
                    )
                }
                if (loginError) {
                    DialogBox(
                        closeDialog = { loginError = false }
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally, // Wyśrodkowanie kolumny
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Błąd logowania:\nKod $loginErrorCode",
                                fontSize = fontSize,
                                color = colorResource(id = R.color.textColor),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp), // Dodaj odstęp poniżej tekstu
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = loginErrorMessage,
                                fontSize = fontSize,
                                color = colorResource(id = R.color.textColor),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp), // Dodaj odstęp poniżej tekstu
                                textAlign = TextAlign.Center
                            )
                            DarkButton(
                                text = "Zamknij",
                                buttonWidth = elementWidth / 2,
                                buttonHeight = forgotPasswordButtonHeight,
                                fontSize = fontSize / 2,
                                onClick = { loginError = false }
                            )
                        }
                    }
                }
                if (showDialog) {
                    DialogBox(
                        closeDialog = { showDialog = false }
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally, // Wyśrodkowanie kolumny
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Resetowanie hasła",
                                fontSize = fontSize,
                                color = colorResource(id = R.color.textColor),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp), // Dodaj odstęp poniżej tekstu
                                textAlign = TextAlign.Center
                            )
                            var email by remember { mutableStateOf("") }
                            var isEmailValid by remember { mutableStateOf(true) }
                            LightTextField(
                                placeholder = "Mail",
                                value = email,
                                onValueChange = {
                                    email = it
                                    isEmailValid =
                                        it.contains("@") // Sprawdzenie, czy email zawiera '@'
                                },
                                isError = !isEmailValid, // Ustawienie stanu błędu
                                textWidth = textWidth,
                                textHeight = textHeight
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp), // Dodaj odstęp między inputem a przyciskami
                                horizontalArrangement = Arrangement.Center // Wyśrodkuj przyciski w poziomie
                            ) {
                                DarkButton(
                                    text = "Anuluj",
                                    buttonWidth = elementWidth / 2,
                                    buttonHeight = forgotPasswordButtonHeight,
                                    fontSize = fontSize / 2,
                                    onClick = { showDialog = false }
                                )
                                DarkButton(
                                    text = "Wyślij",
                                    buttonWidth = elementWidth / 2,
                                    buttonHeight = forgotPasswordButtonHeight,
                                    fontSize = fontSize / 2,
                                    onClick = {
                                        //TODO - button logic
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    LoginScreen(rememberNavController())
}