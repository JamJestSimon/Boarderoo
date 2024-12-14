package pl.boarderoo.mobileapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import pl.boarderoo.mobileapp.ui.theme.outlinedTextFieldColors

@Composable
fun DarkButton(text: String, buttonWidth: Dp, buttonHeight: Dp, fontSize: TextUnit, onClick: () -> Unit) {
    Button(
        onClick = {
            onClick()
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.buttonColor) // Kolor przycisku
        ),
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .size(width = buttonWidth, height = buttonHeight)
    ) {
        Text(
            text = text,
            fontSize = fontSize,
            fontFamily = FontFamily(Font(R.font.mplusrounded1cregular)),
            color = colorResource(id = R.color.textColor) // Kolor tekstu
        )
    }
}

@Composable
fun LightButton(text: String, buttonWidth: Dp, buttonHeight: Dp, fontSize: TextUnit, onClick: () -> Unit) {
    Button(
        onClick = {
            onClick()
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.buttonSecondColor) // Kolor przycisku
        ),
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .size(width = buttonWidth, height = buttonHeight)
    ) {
        Text(
            text = text,
            fontSize = fontSize,
            fontFamily = FontFamily(Font(R.font.mplusrounded1cregular)),
            color = colorResource(id = R.color.textColor) // Kolor tekstu
        )
    }
}

@Composable
fun PageWithTitle(text: String, screenWidth: Int, logoHeight: Dp, fontSize: TextUnit, content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .padding((screenWidth * 0.03).dp)
                .width((screenWidth * 0.9).dp)
                .width(logoHeight)
                .clip(RoundedCornerShape(25.dp))
                .background(colorResource(id = R.color.buttonColor))
                .align(Alignment.TopCenter)
        ) {
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
                    text = text,
                    fontSize = fontSize,
                    color = colorResource(id = R.color.textColor),
                    modifier = Modifier
                        .weight(1f) // Pozwala Text zająć pozostałą przestrzeń
                        .padding(end = 8.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
        content()
    }
}

@Composable
fun LightTextField(placeholder: String, value: String, isError: Boolean, textWidth: Dp, textHeight: Dp, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = {
            onValueChange(it)
        },
        isError = isError,
        modifier = Modifier
            .width(textWidth)
            .height(textHeight)
            .clip(RoundedCornerShape(10.dp)),
        shape = RoundedCornerShape(10.dp), // Zaokrąglenie ramek
        colors = outlinedTextFieldColors(),
        singleLine = true,
        placeholder = {
            Text(
                text = placeholder, // Tekst placeholdera
                color = colorResource(id = R.color.textColor) // Opcjonalnie zmień kolor placeholdera
            )
        }
    )
}