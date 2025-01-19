package pl.boarderoo.mobileapp.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pl.boarderoo.mobileapp.R
import pl.boarderoo.mobileapp.datastore.AppRuntimeData
import java.math.RoundingMode

data class CartItem(
    val name: String,
    val price: Double,
    val quantity: Int
)

@Composable
fun CartScreen() {
    var totalPrice by remember { mutableStateOf(0.0) }

    val cartItemList = AppRuntimeData.cart
        .groupingBy { it }
        .eachCount()
        .map { (game, count) ->
            CartItem(game.name, game.price.toDouble(), count)
        }

    LaunchedEffect(cartItemList) {
        totalPrice = cartItemList.sumOf { it.quantity * it.price }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        if(AppRuntimeData.cart.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Text("Koszyk pusty", modifier = Modifier.align(Alignment.Center))
            }
        } else {
            Spacer(modifier = Modifier.height(10.dp))
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                itemsIndexed(cartItemList) { index, item ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp)
                            .clip(RoundedCornerShape(25.dp))
                            .background(color = colorResource(R.color.buttonSecondColor))
                            .padding(10.dp)
                    ) {
                        Text("Przedmiot: ${item.name}")
                        Text("Ilość: ${item.quantity}")
                        Text("Koszt: ${(item.price*item.quantity).toBigDecimal().setScale(2, RoundingMode.HALF_EVEN).toDouble()} zł")
                    }
                }
            }
            Text("Całkowita wartość zamówienia: $totalPrice")
        }
    }
}

@Preview
@Composable
fun CartScreenPreview() {
    CartScreen()
}