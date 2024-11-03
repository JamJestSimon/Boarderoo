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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
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
    var logoHeight = (screenWidth * 0.3).dp
    var forgotPasswordButtonWidth = elementWidth / 5 * 4
    var forgotPasswordButtonHeight = elementHeight / 3 * 2
    val textWidth = (screenWidth * 0.70).dp
    var textHeight = elementWidth/4
    val fontSize = (textHeight.value * 0.4).sp
    var showDialog by remember { mutableStateOf(false) }
    var inputText by remember { mutableStateOf(TextFieldValue("")) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.backgroundColor)),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.padding((screenWidth *0.03).dp).width((screenWidth * 0.9).dp).width(logoHeight).clip(RoundedCornerShape(25.dp)).background(colorResource(id = R.color.buttonColor)).align(Alignment.TopCenter)
        ){


            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(logoHeight) // Użyj obliczonego rozmiaru
                        .padding(10.dp)
                        .clip(RoundedCornerShape(25.dp))
                )
                Text(
                    text = "Logowanie",
                    fontSize = fontSize*1.5,
                    color = colorResource(id = R.color.textColor),
                    modifier = Modifier
                        .weight(1f) // Pozwala Text zająć pozostałą przestrzeń
                        .padding(end = 8.dp),
                    fontFamily  = FontFamily(Font(R.font.mplusrounded1cregular)),
                    textAlign = TextAlign.Center
                )
            }

        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, // Wyśrodkowanie kolumny
            modifier = Modifier.fillMaxWidth() // Ustawia szerokość kolumny na pełną szerokość
        ) {

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
                    .height(textHeight).clip(RoundedCornerShape(10.dp)),
                shape = RoundedCornerShape(10.dp), // Zaokrąglenie ramek
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = colorResource(id = R.color.inputColor), // Kolor tła wewnątrz ramek
                    focusedTextColor = colorResource(id = R.color.textColor),
                    unfocusedTextColor = colorResource(id = R.color.textColor),
                    cursorColor = colorResource(id = R.color.buttonColor),
                    focusedBorderColor = colorResource(id = R.color.buttonColor),
                    unfocusedBorderColor = colorResource(id = R.color.buttonColor)
                ),
                singleLine = true,
                placeholder = {
                    Text(
                        text = "Mail", // Tekst placeholdera
                        fontFamily  = FontFamily(Font(R.font.mplusrounded1cregular)),
                        color = colorResource(id = R.color.textColor) // Opcjonalnie zmień kolor placeholdera
                    )
                }
            )


            Spacer(modifier = Modifier.height((screenWidth *0.03).dp)) // Przerwa między logo a przyciskami

            val password = remember { mutableStateOf("") }

            OutlinedTextField(
                value = password.value,
                onValueChange = { password.value = it },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .width(textWidth)
                    .height(textHeight).clip(RoundedCornerShape(10.dp)),
                shape = RoundedCornerShape(10.dp), // Zaokrąglenie ramek
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = colorResource(id = R.color.inputColor), // Kolor tła wewnątrz ramek
                    focusedTextColor = colorResource(id = R.color.textColor),
                    unfocusedTextColor = colorResource(id = R.color.textColor),
                    cursorColor = colorResource(id = R.color.buttonColor),
                    focusedBorderColor = colorResource(id = R.color.buttonColor),
                    unfocusedBorderColor = colorResource(id = R.color.buttonColor)
                ),
                singleLine = true,
                placeholder = {
                    Text(
                        text = "Hasło", // Tekst placeholdera
                        fontFamily  = FontFamily(Font(R.font.mplusrounded1cregular)),
                        color = colorResource(id = R.color.textColor) // Opcjonalnie zmień kolor placeholdera
                    )
                }
            )



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
                    fontFamily  = FontFamily(Font(R.font.mplusrounded1cregular)),
                    color = colorResource(id = R.color.textColor) // Kolor tekstu
                )
            }

            Spacer(modifier = Modifier.height((screenWidth * 0.03).dp)) // Przerwa między przyciskami

            Button(
                onClick = { showDialog = true },
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
                    fontFamily  = FontFamily(Font(R.font.mplusrounded1cregular)),
                    color = colorResource(id = R.color.textColor) // Kolor tekstu
                )
            }
        }
        if (showDialog) {
            Dialog(onDismissRequest = { showDialog = false }) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(colorResource(id = R.color.backgroundColor)) // Kolor tła dialogu
                        .border(BorderStroke(2.dp, colorResource(id = R.color.buttonColor)), RoundedCornerShape(16.dp)) // Obramowanie
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
                            fontFamily = FontFamily(Font(R.font.mplusrounded1cregular)),
                            color = colorResource(id = R.color.textColor),
                            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), // Dodaj odstęp poniżej tekstu
                            textAlign = TextAlign.Center
                        )
                        OutlinedTextField(
                            value = inputText,
                            onValueChange = { inputText = it },
                            modifier = Modifier
                                .width(textWidth)
                                .height(textHeight)
                                .clip(RoundedCornerShape(10.dp)),
                            shape = RoundedCornerShape(10.dp),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                containerColor = colorResource(id = R.color.inputColor),
                                focusedTextColor = colorResource(id = R.color.textColor),
                                unfocusedTextColor = colorResource(id = R.color.textColor),
                                cursorColor = colorResource(id = R.color.buttonColor),
                                focusedBorderColor = colorResource(id = R.color.buttonColor),
                                unfocusedBorderColor = colorResource(id = R.color.buttonColor)
                            ),
                            singleLine = true,
                            placeholder = {
                                Text(
                                    text = "Mail",
                                    fontFamily = FontFamily(Font(R.font.mplusrounded1cregular)),
                                    color = colorResource(id = R.color.textColor)
                                )
                            }
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp), // Dodaj odstęp między inputem a przyciskami
                            horizontalArrangement = Arrangement.Center // Wyśrodkuj przyciski w poziomie
                        ) {
                            Button(
                                onClick = { showDialog = false },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = colorResource(id = R.color.buttonColor)
                                ),
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .size(width = elementWidth / 2, height = forgotPasswordButtonHeight)
                            ) {
                                Text(
                                    text = "Anuluj",
                                    fontSize = fontSize / 2,
                                    fontFamily = FontFamily(Font(R.font.mplusrounded1cregular)),
                                    color = colorResource(id = R.color.textColor)
                                )
                            }

                            Button(
                                onClick = { /* Dodaj logikę potwierdzenia */ },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = colorResource(id = R.color.buttonColor)
                                ),
                                modifier = Modifier
                                    .padding(start = 8.dp)
                                    .size(width = elementWidth / 2, height = forgotPasswordButtonHeight)
                            ) {
                                Text(
                                    text = "Wyślij",
                                    fontSize = fontSize / 2,
                                    fontFamily = FontFamily(Font(R.font.mplusrounded1cregular)),
                                    color = colorResource(id = R.color.textColor)
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
    LoginScreen()
}