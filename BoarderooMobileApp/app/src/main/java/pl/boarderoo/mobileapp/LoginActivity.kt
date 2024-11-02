package pl.boarderoo.mobileapp

import android.content.Intent
import android.os.Bundle
import android.text.Layout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.nio.file.WatchEvent
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.sp
import pl.boarderoo.mobileapp.ui.theme.BoarderooMobileAppTheme

class LoginActivity : ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BoarderooMobileAppTheme {
                LoginScreen() // Wywołanie funkcji LoginScreen, aby ustawić widok
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen() {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp

    val elementWidth = (screenWidth * 0.60).dp
    var elementHeight = elementWidth/4
    var forgotPasswordButtonWidth = elementWidth / 5 * 4
    var forgotPasswordButtonHeight = elementHeight / 3 * 2
    val textWidth = (screenWidth * 0.70).dp
    var textHeight = elementWidth/4
    val fontSize = (textHeight.value * 0.4).sp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.backgroundColor)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, // Wyśrodkowanie kolumny
            modifier = Modifier.fillMaxWidth() // Ustawia szerokość kolumny na pełną szerokość
        ) {
            Box(
            ) {
                Column(

                ){
                    Text(
                        text = "Email",
                        fontSize = fontSize,
                        color = colorResource(id = R.color.textColor),
                        modifier = Modifier.padding(end = 8.dp)
                    )

                    val email = remember { mutableStateOf("") }
                    val isEmailValid = remember { mutableStateOf(true) }

                    OutlinedTextField(
                        value = email.value,
                        onValueChange = {
                            email.value = it
                            isEmailValid.value = it.contains("@") // Sprawdzenie, czy email zawiera '@'
                        },
                        isError = !isEmailValid.value, // Ustawienie stanu błędu
                        modifier = Modifier
                            .width(textWidth)
                            .height(textHeight),
                        singleLine = true
                    )
                }

            }


            Spacer(modifier = Modifier.height((screenWidth *0.05).dp)) // Przerwa między logo a przyciskami

            Box(
            ) {
                Column(

                ){
                    Text(
                        text = "Hasło",
                        fontSize = fontSize,
                        color = colorResource(id = R.color.textColor),
                        modifier = Modifier.padding(end = 8.dp) // Dodanie odstępu od pola tekstowego
                    )

                    val password = remember { mutableStateOf("") }

                    OutlinedTextField(
                        value = password.value,
                        onValueChange = { password.value = it },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.width(textWidth).height(textHeight),
                        singleLine = true
                    )
                }

            }



            Spacer(modifier = Modifier.height((screenWidth *0.05).dp)) // Przerwa między logo a przyciskami
            Button(
                onClick = {
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.buttonColor) // Kolor przycisku
                ),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .size(width = elementWidth, height = elementHeight)
            ) {
                Text(
                    text = "Zaloguj",
                    fontSize = fontSize,
                    color = colorResource(id = R.color.textColor) // Kolor tekstu
                )
            }

            Spacer(modifier = Modifier.height((screenWidth * 0.03).dp)) // Przerwa między przyciskami

            Button(
                onClick = {
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.buttonSecondColor) // Kolor przycisku
                ),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .size(width = forgotPasswordButtonWidth, height = forgotPasswordButtonHeight)
            ) {
                Text(
                    text = "Zapomniałeś hasła?",
                    fontSize = fontSize / 2,
                    color = colorResource(id = R.color.textColor) // Kolor tekstu
                )
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    LoginScreen()
}