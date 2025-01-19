package pl.boarderoo.mobileapp.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import pl.boarderoo.mobileapp.R
import pl.boarderoo.mobileapp.retrofit.viewmodels.OrderListViewModel

@Composable
fun OrderScreen() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "OrderListScreen"
    ) {
        composable(route = "OrderListScreen") { OrderListScreen(navController) }
        composable(route = "OrderDetailsScreen/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            OrderDetailsScreen(navController, id)
        }
    }
}

@Composable
fun OrderListScreen(navController: NavController) {
    val viewModel: OrderListViewModel = viewModel()
    val orderList = viewModel.orderList.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()
    val errorMessage = viewModel.errorMessage.collectAsState()

    LaunchedEffect(orderList) {
        viewModel.getOrderList()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        if (errorMessage.value != null) {
            ErrorState(
                errorMessage = errorMessage.value!!,
                onClose = { viewModel.getOrderList() }
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
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Lista zamówień"
            )
            LazyColumn(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                itemsIndexed(orderList.value!!) { index, order ->
                    OrderListItem(order.id, order.status) {
                        navController.navigate("OrderDetailsScreen/${order.id}")
                    }
                }
            }
        }
    }
}

@Composable
fun OrderListItem(id: String, status: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(25.dp)
            )
            .background(colorResource(R.color.buttonSecondColor))
            .clickable {
                onClick()
            }
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(
            text = id
        )
        Text(
            text = "Status: $status"
        )
    }
}

@Preview
@Composable
fun OrderScreenPreview() {
    OrderScreen()
}