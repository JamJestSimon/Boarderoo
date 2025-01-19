package pl.boarderoo.mobileapp.main

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import pl.boarderoo.mobileapp.ErrorState
import pl.boarderoo.mobileapp.LightButton
import pl.boarderoo.mobileapp.R
import pl.boarderoo.mobileapp.datastore.AppRuntimeData
import pl.boarderoo.mobileapp.retrofit.viewmodels.GameDetailsViewModel

@Composable
fun GameDetailsScreen(
    navController: NavController,
    id: String
) {
    BackHandler(enabled = true) { navController.navigateUp() }
    val viewModel: GameDetailsViewModel = viewModel()
    val game = viewModel.gameDetails.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()
    val errorMessage = viewModel.errorMessage.collectAsState()
    LaunchedEffect(game) { viewModel.getGameDetails(id) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        if (errorMessage.value != null) {
            ErrorState(
                errorMessage = errorMessage.value!!,
                onClose = { viewModel.getGameDetails(id) }
            )
        } else if (isLoading.value) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(200.dp),
                    color = colorResource(R.color.textColor),
                    trackColor = colorResource(R.color.buttonColor)
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .clip(RoundedCornerShape(25.dp))
                    .background(colorResource(id = R.color.buttonSecondColor))
                    .padding(15.dp)
            ) {
                //TODO - odkomentować jak będzie działało
                //ImageFromUrl(gameModel.image[0])
                Text("Nazwa: ${game.value!!.name}")
                Text("Wydawca: ${game.value!!.publisher}")
                Text("Wiek: ${game.value!!.rating}")
                Text("Ilość graczy: ${game.value!!.playersNumber}")
                Text("Gatunek: ${game.value!!.type}")
                Text("Cena: ${game.value!!.price} zł")
                Text("Opis:\n${game.value!!.description}")
            }
            if(game.value!!.availableCopies > 0) {
                LightButton(
                    text = "Dodaj do koszyka",
                    fontSize = 12.sp
                ) {
                    AppRuntimeData.cart.add(game.value!!)
                }
            } else {
                Text("Produkt niedostępny")
            }
        }
    }
}

@Preview
@Composable
fun GameDetailsScreenPreview() {
    GameDetailsScreen(rememberNavController(), "")
}