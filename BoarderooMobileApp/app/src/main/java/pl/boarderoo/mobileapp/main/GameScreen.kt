package pl.boarderoo.mobileapp.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import pl.boarderoo.mobileapp.ErrorState
import pl.boarderoo.mobileapp.ImageFromUrl
import pl.boarderoo.mobileapp.R
import pl.boarderoo.mobileapp.retrofit.models.GameModel
import pl.boarderoo.mobileapp.retrofit.viewmodels.GameListViewModel

@Composable
fun GameScreen() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "GameListScreen"
    ) {
        composable(route = "GameListScreen") { GameListScreen(navController) }
        composable(route = "GameDetailsScreen/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            GameDetailsScreen()
        }
    }
}

@Composable
fun GameListScreen(navController: NavController) {
    val viewModel: GameListViewModel = viewModel()
    val gameList = viewModel.gameList.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()
    val errorMessage = viewModel.errorMessage.collectAsState()

    LaunchedEffect(gameList) {
        viewModel.getGameList()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        if (errorMessage.value != null) {
            ErrorState(
                errorMessage = errorMessage.value!!,
                onClose = { viewModel.getGameList() }
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
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 128.dp)
            ) {
                itemsIndexed(gameList.value!!) { index, game ->
                    GameGridItem(game) {
                        navController.navigate("GameDetailsScreen/${game.id}")
                    }
                }
            }
        }
    }
}

@Composable
fun GameGridItem(gameModel: GameModel, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(25.dp)
            .clip(RoundedCornerShape(25.dp))
            .background(colorResource(id = R.color.buttonColor))
            .clickable {
                onClick()
            }
    ) {
        //TODO - odkomentować jak będzie działało
        //ImageFromUrl(gameModel.image[0])
        Text(gameModel.name)
        Text(gameModel.publisher)
        Text(gameModel.rating)
        Text(gameModel.playersNumber)
        Text(gameModel.type)
        Text(gameModel.price.toString())
    }
}

@Preview
@Composable
fun GameScreenPreview() {
    GameScreen()
}