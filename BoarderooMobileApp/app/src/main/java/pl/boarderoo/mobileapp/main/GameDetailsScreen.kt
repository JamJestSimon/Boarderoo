package pl.boarderoo.mobileapp.main

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
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
    val context = LocalContext.current
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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(bottom = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    val pagerState = rememberPagerState(
                        initialPage = 0,
                        pageCount = { game.value!!.image.size })
                    HorizontalPager(
                        beyondViewportPageCount = game.value!!.image.size,
                        state = pagerState,
                        key = {game.value!!.image[it]}
                    ) {
                        index -> Image(
                        painter = rememberImagePainter("https://firebasestorage.googleapis.com/v0/b/boarderoo-71469.firebasestorage.app/o/files%2F${game.value!!.image[index]}?alt=media"),
                        contentDescription = "Game Image",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(15.dp))
                            .background(Color.White)
                            .border(4.dp, Color(0xFF422D29), RoundedCornerShape(15.dp))
                    )
                    }

                }
                Text("Nazwa: ${game.value!!.name}", color = Color(0xFF422D29))
                Text("Wydawca: ${game.value!!.publisher}", color = Color(0xFF422D29))
                Text("Wiek: ${game.value!!.rating}", color = Color(0xFF422D29))
                Text("Ilość graczy: ${game.value!!.playersNumber}", color = Color(0xFF422D29))
                Text("Gatunek: ${game.value!!.type}", color = Color(0xFF422D29))
                Text("Cena: ${game.value!!.price} zł", color = Color(0xFF422D29))
                Text("Opis:\n${game.value!!.description}", color = Color(0xFF422D29))
            }
            if (game.value!!.availableCopies > 0) {
                LightButton(
                    text = "Dodaj do koszyka",
                    fontSize = 12.sp,
                ) {
                    val toast = Toast.makeText(context, "Dodano do koszyka", Toast.LENGTH_SHORT)
                    toast.show()
                    AppRuntimeData.cart.add(game.value!!)
                    navController.navigateUp()
                }
            } else {
                Text(
                    "Produkt niedostępny",
                    color = Color.Red
                )
            }
        }
    }
}

@Preview
@Composable
fun GameDetailsScreenPreview() {
    GameDetailsScreen(rememberNavController(), "")
}