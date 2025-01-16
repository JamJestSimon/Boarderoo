package pl.boarderoo.mobileapp.start

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import pl.boarderoo.mobileapp.DarkButton
import pl.boarderoo.mobileapp.LightTextField
import pl.boarderoo.mobileapp.PageWithTitle

@Composable
fun RegisterScreen(navController: NavController, email: String) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp

    val elementWidth = (screenWidth * 0.60).dp
    val elementHeight = elementWidth / 4
    val logoHeight = (screenWidth * 0.3).dp
    val textWidth = (screenWidth * 0.70).dp
    val textHeight = elementWidth / 4
    val fontSize = (textHeight.value * 0.4).sp
    val entries = remember {
        mutableStateListOf(
            RegisterEntry("Imie", mutableStateOf("")),
            RegisterEntry("Nazwisko", mutableStateOf("")),
            RegisterEntry("Adres", mutableStateOf("")),
            RegisterEntry("Email", mutableStateOf("")),
            RegisterEntry("Haslo", mutableStateOf("")),
            RegisterEntry("Powtorz Haslo", mutableStateOf(""))
        )
    }
    println(email)
    if (email.isNotEmpty()) {
        val emailEntry = entries.find { it.name == "Email" }
        println(emailEntry)
        emailEntry?.data?.value = email
        println(emailEntry)
    }
    BackHandler(
        enabled = true
    ) {
        navController.navigateUp()
    }
    PageWithTitle(
        text = "Rejestracja",
        screenWidth = screenWidth,
        logoHeight = logoHeight,
        fontSize = fontSize * 1.5
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, // Wyśrodkowanie kolumny
            modifier = Modifier.fillMaxWidth() // Ustawia szerokość kolumny na pełną szerokość
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy((screenWidth * 0.03).dp)
            ) {
                for (entry in entries) {
                    if (entry.name == "Email") {
                        var isEmailValid by remember { mutableStateOf(true) }
                        LightTextField(
                            placeholder = entry.name,
                            value = entry.data.value,
                            isError = !isEmailValid,
                            textWidth = textWidth,
                            textHeight = textHeight,
                            onValueChange = {
                                entry.data.value = it
                                isEmailValid =
                                    it.contains("@") // Sprawdzenie, czy email zawiera '@'
                            }
                        )
                    } else {
                        LightTextField(
                            placeholder = entry.name,
                            value = entry.data.value,
                            visualTransformation = if(entry.name == "Haslo" || entry.name == "Powtorz Haslo") PasswordVisualTransformation() else VisualTransformation.None,
                            isError = false,
                            textWidth = textWidth,
                            textHeight = textHeight,
                            onValueChange = {
                                entry.data.value = it
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height((screenWidth * 0.05).dp)) // Przerwa między logo a przyciskami
            DarkButton(
                text = "Zarejestruj",
                buttonWidth = elementWidth,
                buttonHeight = elementHeight,
                fontSize = fontSize,
                onClick = {
                    //TODO - button logic
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRegisterScreen() {
    RegisterScreen(rememberNavController(), "test@gmail.com")
}

data class RegisterEntry(
    val name: String, var data: MutableState<String>
)