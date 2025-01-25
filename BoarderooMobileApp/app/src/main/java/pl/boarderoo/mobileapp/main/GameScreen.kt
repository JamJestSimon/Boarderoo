package pl.boarderoo.mobileapp.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import pl.boarderoo.mobileapp.ElasticLightTextField
import pl.boarderoo.mobileapp.ErrorState
import pl.boarderoo.mobileapp.ImageFromUrl
import pl.boarderoo.mobileapp.LightButton
import pl.boarderoo.mobileapp.R
import pl.boarderoo.mobileapp.data.Categories
import pl.boarderoo.mobileapp.data.Publishers
import pl.boarderoo.mobileapp.retrofit.models.GameModel
import pl.boarderoo.mobileapp.retrofit.viewmodels.GameListViewModel
import pl.boarderoo.mobileapp.ui.theme.outlinedTextFieldColors

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
            GameDetailsScreen(navController, id)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameListScreen(navController: NavController) {
    val viewModel: GameListViewModel = viewModel()
    val gameList = viewModel.gameList.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()
    val errorMessage = viewModel.errorMessage.collectAsState()

    val player_default = 1.0f..10.0f
    val age_default = 0.0f..100.0f
    val year_default = 2000.0f..2025.0f

    val publisher_items = remember { mutableStateListOf<DropdownItem>() }
    val category_items = remember { mutableStateListOf<DropdownItem>() }

    LaunchedEffect(Unit) {
        for (publisher in Publishers.list) {
            publisher_items.add(DropdownItem(publisher, mutableStateOf(false)))
        }
        for (category in Categories.list) {
            category_items.add(DropdownItem(category, mutableStateOf(false)))
        }
    }

    val publisher_filters = remember {
        derivedStateOf {
            publisher_items.filter { it.isSelected.value }.map { it.label }
        }
    }

    val category_filters = remember {
        derivedStateOf {
            category_items.filter { it.isSelected.value }.map { it.label }
        }
    }

    var publisher_expanded by remember { mutableStateOf(false) }
    var category_expanded by remember { mutableStateOf(false) }

    var filters_expanded by remember { mutableStateOf(false) }

    var search_expression by remember { mutableStateOf("") }

    var player_range by remember { mutableStateOf(1.0f..10.0f) }
    var age_range by remember { mutableStateOf(0.0f..100f) }
    var year_range by remember { mutableStateOf(2000.0f..2025.0f) }

    var filtered_games by remember { mutableStateOf(gameList.value) }

    filtered_games = gameList.value?.filter { game ->
        val minPlayers = game.playersNumber.replace(" ", "").split("-").first().toFloat()
        val maxPlayers = game.playersNumber.replace(" ", "").split("-").last().toFloat()
        val minAge = game.rating.replace(" ", "").split("-").first().toFloat()
        val maxAge = game.rating.replace(" ", "").split("-").last().toFloat()

        // Active filter conditions
        val matchesSearch =
            search_expression.isEmpty() || game.name.contains(search_expression, ignoreCase = true)
        val matchesPublisher =
            publisher_filters.value.isEmpty() || publisher_filters.value.contains(game.publisher)
        val matchesCategory =
            category_filters.value.isEmpty() || category_filters.value.contains(game.type)
        val matchesPlayers =
            player_range == player_default || (player_range.contains(minPlayers) && player_range.contains(maxPlayers))
        val matchesAge =
            age_range == age_default || (age_range.contains(minAge) && age_range.contains(maxAge))
        val matchesYear = year_range == year_default || year_range.contains(game.year.toFloat())

        matchesSearch && matchesPublisher && matchesCategory && matchesPlayers && matchesAge && matchesYear
    }

    LaunchedEffect(gameList) {
        viewModel.getGameList()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
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
            ElasticLightTextField(
                placeholder = "Wyszukaj",
                value = search_expression,
                isError = false,
                modifier = Modifier.fillMaxWidth()
            ) {
                search_expression = it
            }
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    onClick = { filters_expanded = !filters_expanded },
                    modifier = Modifier.align(alignment = Alignment.End)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.List,
                        contentDescription = "Filter Icon"
                    )
                }
                AnimatedVisibility(
                    visible = filters_expanded,
                    enter = slideInVertically(animationSpec = tween(200)) { -it } + expandVertically(
                        expandFrom = Alignment.Top,
                        animationSpec = tween(200)
                    ),
                    exit = slideOutVertically(animationSpec = tween(200)) { -it } + shrinkVertically(
                        shrinkTowards = Alignment.Top,
                        animationSpec = tween(200)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        ExposedDropdownMenuBox(
                            expanded = publisher_expanded && filters_expanded,
                            onExpandedChange = {
                                publisher_expanded = !publisher_expanded
                            }
                        ) {
                            OutlinedTextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(MenuAnchorType.PrimaryEditable, true),
                                value = if (publisher_filters.value.isNotEmpty()) publisher_filters.value.joinToString { it } else "",
                                placeholder = {
                                    Text("Wybierz wydawców")
                                },
                                onValueChange = { },
                                readOnly = true,
                                colors = outlinedTextFieldColors(),
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = publisher_expanded)
                                },
                                singleLine = true
                            )
                            ExposedDropdownMenu(
                                expanded = publisher_expanded && filters_expanded,
                                onDismissRequest = { publisher_expanded = false }
                            ) {
                                publisher_items.forEach { item ->
                                    DropdownMenuItem(
                                        onClick = {
                                            item.isSelected.value = !item.isSelected.value
                                        },
                                        text = {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Checkbox(
                                                    checked = item.isSelected.value,
                                                    onCheckedChange = {
                                                        item.isSelected.value = it
                                                    }
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(text = item.label)
                                            }
                                        }
                                    )
                                }
                            }
                        }
                        ExposedDropdownMenuBox(
                            expanded = category_expanded && filters_expanded,
                            onExpandedChange = {
                                category_expanded = !category_expanded
                            }
                        ) {
                            OutlinedTextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(MenuAnchorType.PrimaryEditable, true),
                                value = if (category_filters.value.isNotEmpty()) category_filters.value.joinToString { it } else "",
                                placeholder = {
                                    Text("Wybierz kategorie")
                                },
                                onValueChange = { },
                                readOnly = true,
                                colors = outlinedTextFieldColors(),
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = category_expanded)
                                },
                                singleLine = true
                            )
                            ExposedDropdownMenu(
                                expanded = category_expanded && filters_expanded,
                                onDismissRequest = { category_expanded = false }
                            ) {
                                category_items.forEach { item ->
                                    DropdownMenuItem(
                                        onClick = {
                                            item.isSelected.value = !item.isSelected.value
                                        },
                                        text = {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Checkbox(
                                                    checked = item.isSelected.value,
                                                    onCheckedChange = {
                                                        item.isSelected.value = it
                                                    }
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(text = item.label)
                                            }
                                        }
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Column(
                            modifier = Modifier.padding(horizontal = 10.dp)
                        ) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                text = "Liczba graczy"
                            )
                            RangeSlider(
                                value = player_range,
                                onValueChange = {
                                    val corrected_range = when {
                                        it.start > it.endInclusive -> it.endInclusive..it.endInclusive
                                        else -> it.start..it.endInclusive
                                    }
                                    player_range = corrected_range
                                },
                                valueRange = 1.0f..10.0f,
                                modifier = Modifier.fillMaxWidth(),
                                steps = 8
                            )
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Min: ${player_range.start.toInt()}")
                                Text("Max: ${player_range.endInclusive.toInt()}")
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                text = "Wiek"
                            )
                            RangeSlider(
                                value = age_range,
                                onValueChange = {
                                    val corrected_range = when {
                                        it.start > it.endInclusive -> it.endInclusive..it.endInclusive
                                        else -> it.start..it.endInclusive
                                    }
                                    age_range = corrected_range
                                },
                                valueRange = 0.0f..100.0f,
                                modifier = Modifier.fillMaxWidth(),
                                steps = 99
                            )
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Min: ${age_range.start.toInt()}")
                                Text("Max: ${age_range.endInclusive.toInt()}")
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                text = "Rok wydania"
                            )
                            RangeSlider(
                                value = year_range,
                                onValueChange = {
                                    val corrected_range = when {
                                        it.start > it.endInclusive -> it.endInclusive..it.endInclusive
                                        else -> it.start..it.endInclusive
                                    }
                                    year_range = corrected_range
                                },
                                valueRange = 2000.0f..2025.0f,
                                modifier = Modifier.fillMaxWidth(),
                                steps = 24
                            )
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Min: ${year_range.start.toInt()}")
                                Text("Max: ${year_range.endInclusive.toInt()}")
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        LightButton(
                            text = "Wyczysc filtry",
                            fontSize = 12.sp,
                            modifier = Modifier.align(alignment = Alignment.End)
                        ) {
                            search_expression = ""
                            for (publisher in publisher_items) {
                                publisher.isSelected.value = false
                            }
                            for (category in category_items) {
                                category.isSelected.value = false
                            }
                            player_range = player_default
                            age_range = age_default
                            year_range = year_default
                        }
                    }
                }
            }
            LazyVerticalGrid(
                columns = GridCells.Fixed(count = 1)
            ) {
                itemsIndexed(filtered_games!!) { index, game ->
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
            .padding(10.dp)
            .clip(RoundedCornerShape(25.dp))
            .background(colorResource(id = R.color.buttonSecondColor))
            .clickable {
                onClick()
            }
            .padding(15.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(bottom = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = rememberImagePainter("https://firebasestorage.googleapis.com/v0/b/boarderoo-71469.firebasestorage.app/o/files%2F${gameModel.image[0]}?alt=media"),
                contentDescription = "Game Image",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(15.dp))
                    .background(Color.White)
                    .border(4.dp, Color(0xFF422D29), RoundedCornerShape(15.dp))
            )
        }


        Text("Nazwa: ${gameModel.name}", color = Color(0xFF422D29))
        Text("Wydawca: ${gameModel.publisher}", color = Color(0xFF422D29))
        Text("Wiek: ${gameModel.rating}", color = Color(0xFF422D29))
        Text("Ilość graczy: ${gameModel.playersNumber}", color = Color(0xFF422D29))
        Text("Gatunek: ${gameModel.type}", color = Color(0xFF422D29))
        Text("Cena: ${gameModel.price} zł", color = Color(0xFF422D29))
    }
}

data class DropdownItem(val label: String, var isSelected: MutableState<Boolean>)

@Preview
@Composable
fun GameScreenPreview() {
    GameScreen()
}