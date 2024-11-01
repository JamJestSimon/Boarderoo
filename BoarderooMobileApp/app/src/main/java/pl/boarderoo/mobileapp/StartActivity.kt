package pl.boarderoo.mobileapp

import android.text.Layout
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.sp

class StartActivity {

}

@Composable
fun LogoScreen() {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp

    val elementWidth = (screenWidth * 0.60).dp
    val buttonWidth = (screenWidth * 0.70).dp
    var buttonHeight = elementWidth/4;
    val fontSize = (buttonHeight.value * 0.4).sp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEA962A)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(elementWidth) // Użyj obliczonego rozmiaru
                        .padding(10.dp)
                        .border(
                            BorderStroke(5.dp, Color(0xFFA4691D)),
                            shape = RoundedCornerShape(25.dp)
                        )
                        .clip(RoundedCornerShape(25.dp))
                )

                Spacer(modifier = Modifier.height((screenWidth *0.05).dp)) // Przerwa między logo a przyciskami

                Button(
                    onClick = { /* TODO: Dodaj akcję logowania */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFA4691D) // Kolor przycisku
                    ),

                    modifier = Modifier.padding(horizontal = 16.dp).size(width = buttonWidth, height = buttonHeight)
                ) {
                    Text(
                        text = "Logowanie",
                        fontSize = fontSize,
                        color = Color(0xFF170F04) // Kolor tekstu

                    )
                }

                Spacer(modifier = Modifier.height((screenWidth *0.03).dp)) // Przerwa między logo a przyciskami

                Button(
                    onClick = { /* TODO: Dodaj akcję rejestracji */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFA4691D) // Kolor przycisku
                    ),
                    modifier = Modifier.padding(horizontal = 16.dp).size(width = buttonWidth, height = buttonHeight)
                ) {
                    Text(
                        text = "Rejestracja",
                        fontSize = fontSize,
                        color = Color(0xFF170F04) // Kolor tekstu
                    )
                }

                Spacer(modifier = Modifier.height((screenWidth *0.03).dp)) // Przerwa między logo a przyciskami

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.google_logo),
                        contentDescription = "Google Icon",
                        modifier = Modifier
                            .size(buttonHeight) // Użyj obliczonego rozmiar
                            .border(
                                BorderStroke(2.dp, Color(0xFF747775)),
                                shape = RoundedCornerShape(buttonHeight/2)
                            )
                            .clip(RoundedCornerShape(buttonHeight/2))
                    )

                    Spacer(modifier = Modifier.width((screenWidth *0.03).dp)) // Przerwa między logo a przyciskami

                    Image(
                        painter = painterResource(id = R.drawable.discord),
                        contentDescription = "Discord Icon",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(buttonHeight) // Użyj obliczonego rozmiaru
                            .border(
                                BorderStroke(2.dp, Color(0xFF5865F2)),
                                shape = RoundedCornerShape(buttonHeight/2)
                            )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLogoScreen() {
    LogoScreen()
}