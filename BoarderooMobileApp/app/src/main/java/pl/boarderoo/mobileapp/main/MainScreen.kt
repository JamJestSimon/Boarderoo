package pl.boarderoo.mobileapp.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import pl.boarderoo.mobileapp.ErrorState
import pl.boarderoo.mobileapp.R
import pl.boarderoo.mobileapp.datastore.AppRuntimeData
import pl.boarderoo.mobileapp.retrofit.viewmodels.OrderListViewModel

@Composable
fun MainScreen() {
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
        verticalArrangement = Arrangement.Center
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
            Text(
                text = "Witaj, ${AppRuntimeData.user?.name}",
                color = colorResource(R.color.textColor)
            )
            Spacer(modifier = Modifier.height(30.dp))
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .padding(10.dp)
                    .border(
                        BorderStroke(5.dp, colorResource(id = R.color.buttonColor)),
                        shape = RoundedCornerShape(25.dp)
                    )
                    .clip(RoundedCornerShape(25.dp))
            )
            Spacer(modifier = Modifier.height(30.dp))
            val orderCount = orderList.value!!.count { order -> order.status != "Anulowane" && order.status != "Zakończone" }
            var suffix: String
            if (orderCount == 1) suffix = "oczekujące zamówienie"
            if (orderCount < 5) suffix = "oczekujące zamówienia"
            else suffix = "oczekujących zamówień"
            Text(
                text = "Masz ${orderCount} ${suffix} na swoim koncie",
                color = colorResource(R.color.textColor),
                modifier = Modifier.fillMaxWidth(0.8f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    MainScreen()
}