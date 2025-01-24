package pl.boarderoo.mobileapp.main

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.launch
import pl.boarderoo.mobileapp.ErrorState
import pl.boarderoo.mobileapp.LightButton
import pl.boarderoo.mobileapp.R
import pl.boarderoo.mobileapp.retrofit.services.OrderService
import pl.boarderoo.mobileapp.retrofit.viewmodels.OrderDetailsViewModel
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

data class OrderItem(
    val name: String,
    val count: Int
)

@Composable
fun OrderDetailsScreen(navController: NavController, id: String) {
    BackHandler(enabled = true) { navController.navigateUp() }

    val viewModel: OrderDetailsViewModel = viewModel()
    val order = viewModel.orderList.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()
    val errorMessage = viewModel.errorMessage.collectAsState()

    val orderService = OrderService()

    val scope = rememberCoroutineScope()

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
            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                    .clip(
                        RoundedCornerShape(25.dp)
                    )
                    .background(colorResource(R.color.buttonSecondColor))
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = order.value!!.id
                )
                Text(
                    text = "Status: ${order.value!!.status}"
                )
                Text(
                    text = "Okres zamówienia: ${LocalDate.parse(order.value!!.start, DateTimeFormatter.ISO_DATE_TIME)} - ${LocalDate.parse(order.value!!.end, DateTimeFormatter.ISO_DATE_TIME)}"
                )
                Text(
                    text = "Wartość zamówienia: ${order.value!!.price} zł"
                )
                Text(
                    text = "Zawartość zamówienia:"
                )
                val orderItems = order.value!!.items
                    .groupingBy { it }
                    .eachCount()
                    .map { (name, count) ->
                        OrderItem(name, count)
                    }
                LazyColumn(
                    modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    itemsIndexed(orderItems) { index, item ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = item.name
                            )
                            Text(
                                text = "Ilość: ${item.count}"
                            )
                        }
                    }
                }
            }
            if(order.value!!.status != "Zakończone") {
                Spacer(modifier = Modifier.height(10.dp))
                LightButton(
                    text = "Anuluj zamówienie",
                    fontSize = 12.sp
                ) {
                    scope.launch {
                        orderService.cancelOrder(order.value!!)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun OrderDetailsScreenPreview() {
    OrderDetailsScreen(rememberNavController(), "")
}