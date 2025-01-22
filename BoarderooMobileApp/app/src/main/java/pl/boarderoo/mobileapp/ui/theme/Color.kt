package pl.boarderoo.mobileapp.ui.theme

import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import pl.boarderoo.mobileapp.R

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

@Composable
fun outlinedTextFieldColors() : TextFieldColors{
    val containerColor = colorResource(id = R.color.inputColor) // Kolor tła wewnątrz ramek
    return OutlinedTextFieldDefaults.colors(
        focusedTextColor = colorResource(id = R.color.textColor),
        unfocusedTextColor = colorResource(id = R.color.textColor),
        focusedContainerColor = containerColor,
        unfocusedContainerColor = containerColor,
        disabledContainerColor = containerColor,
        cursorColor = colorResource(id = R.color.buttonColor),
        focusedBorderColor = colorResource(id = R.color.buttonColor),
        unfocusedBorderColor = colorResource(id = R.color.buttonColor),
    )
}