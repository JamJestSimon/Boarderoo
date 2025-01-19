package pl.boarderoo.mobileapp.main

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import pl.boarderoo.mobileapp.ErrorState
import pl.boarderoo.mobileapp.R
import pl.boarderoo.mobileapp.retrofit.models.StatusType
import pl.boarderoo.mobileapp.retrofit.models.intToEnum
import pl.boarderoo.mobileapp.retrofit.viewmodels.OrderDetailsViewModel
import pl.boarderoo.mobileapp.retrofit.viewmodels.OrderListViewModel

@Composable
fun OrderDetailsScreen(navController: NavController, id: String) {
    BackHandler(enabled = true) { navController.navigateUp() }

    val viewModel: OrderDetailsViewModel = viewModel()
    val order = viewModel.orderList.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()
    val errorMessage = viewModel.errorMessage.collectAsState()

    LaunchedEffect(order) {
        viewModel.getOrderDetails(id)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        if (errorMessage.value != null) {
            ErrorState(
                errorMessage = errorMessage.value!!,
                onClose = { viewModel.getOrderDetails(id) }
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
            Text(order.value!!.id)
            Text(intToEnum(order.value!!.status).toString())
            Text(order.value!!.price.toString())
        }
    }
}

@Preview
@Composable
fun OrderDetailsScreenPreview() {
    OrderDetailsScreen(rememberNavController(), "")
}