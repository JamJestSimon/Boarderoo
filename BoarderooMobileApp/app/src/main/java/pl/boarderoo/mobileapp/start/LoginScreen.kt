package pl.boarderoo.mobileapp.start

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import pl.boarderoo.mobileapp.DarkButton
import pl.boarderoo.mobileapp.LightButton
import pl.boarderoo.mobileapp.LightTextField
import pl.boarderoo.mobileapp.PageWithTitle
import pl.boarderoo.mobileapp.R

@Composable
fun LoginScreen(navController: NavController) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp

    val elementWidth = (screenWidth * 0.60).dp
    val elementHeight = elementWidth / 4
    val logoHeight = (screenWidth * 0.3).dp
    val forgotPasswordButtonWidth = elementWidth / 5 * 4
    val forgotPasswordButtonHeight = elementHeight / 3 * 2
    val textWidth = (screenWidth * 0.70).dp
    val textHeight = elementWidth / 4
    val fontSize = (textHeight.value * 0.4).sp
    var showDialog by remember { mutableStateOf(false) }
    BackHandler(
        enabled = true
    ) {
        navController.navigateUp()
    }
    PageWithTitle(
        text = "Logowanie",
        screenWidth = screenWidth,
        logoHeight = logoHeight,
        fontSize = fontSize*1.5
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, // Wyśrodkowanie kolumny
            modifier = Modifier.fillMaxWidth() // Ustawia szerokość kolumny na pełną szerokość
        ) {
            var email by remember { mutableStateOf("") }
            var isEmailValid by remember { mutableStateOf(true) }
            LightTextField(
                placeholder = "Mail",
                value = email,
                onValueChange = {
                    email = it
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
                    //TODO - button logic
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
        if (showDialog) {
            Dialog(onDismissRequest = { showDialog = false }) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(colorResource(id = R.color.backgroundColor)) // Kolor tła dialogu
                        .border(
                            BorderStroke(2.dp, colorResource(id = R.color.buttonColor)),
                            RoundedCornerShape(16.dp)
                        ) // Obramowanie
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
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
                                isEmailValid = it.contains("@") // Sprawdzenie, czy email zawiera '@'
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

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    LoginScreen(rememberNavController())
}