package pl.boarderoo.mobileapp

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
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
fun LightButton(text: String, buttonWidth: Dp? = null, buttonHeight: Dp? = null, fontSize: TextUnit, onClick: () -> Unit) {
    Button(
        onClick = {
            onClick()
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.buttonSecondColor) // Kolor przycisku
        ),
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .then(
                if (buttonWidth != null && buttonHeight != null) {
                    Modifier.size(width = buttonWidth, height = buttonHeight)
                } else Modifier
            )
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
fun LightTextField(placeholder: String, value: String, isError: Boolean, textWidth: Dp, textHeight: Dp, visualTransformation: VisualTransformation = VisualTransformation.None, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = {
            onValueChange(it)
        },
        visualTransformation = visualTransformation,
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

@Composable
fun DialogBox(closeDialog: () -> Unit, content: @Composable () -> Unit) {
    Dialog(onDismissRequest = { closeDialog() }) {
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
            content()
        }
    }
}

@Composable
fun ErrorState(modifier: Modifier = Modifier, errorMessage: String, onClose: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Wystąpił błąd:\n\n$errorMessage",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(Font(R.font.mplusrounded1cregular)),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 20.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        LightButton(
            text = "Odśwież",
            fontSize = 16.sp,
            onClick = { onClose() }
        )
    }
}

@Composable
fun ImageFromUrl(url: String) {
    AsyncImage(
        model = url,
        contentDescription = "Image from URL"
    )
}