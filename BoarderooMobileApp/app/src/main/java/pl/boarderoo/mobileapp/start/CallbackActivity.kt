package pl.boarderoo.mobileapp.start

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.boarderoo.mobileapp.ui.theme.BoarderooMobileAppTheme

class CallbackActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var value = ""
        val data: Uri? = intent.data // Odbieranie URI z Intentu
        data?.let {
            val queryParams = it.queryParameterNames // Lista nazw parametrów
            for (param in queryParams) {
                value = it.getQueryParameter(param).toString() // Pobieranie wartości parametru
                println("Parametr: $param, Wartość: $value") // Wyświetlanie w konsoli
            }
        }

        setContent {
            BoarderooMobileAppTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize() // Wypełnia całą dostępną przestrzeń
                        .padding(16.dp) // Opcjonalne: dodanie odstępu od krawędzi
                ) {
                    Text(
                        text = value, // Treść napisu
                    )
                }
            }
        }
    }
}
