package pl.boarderoo.mobileapp.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import pl.boarderoo.mobileapp.datastore.AppRuntimeData

@Composable
fun CartScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        if(AppRuntimeData.cart.isEmpty()) {
            Text("Koszyk pusty")
        } else {
            LazyColumn {
                itemsIndexed(AppRuntimeData.cart) { index, item ->
                    Row {
                        Text(item.name)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun CartScreenPreview() {
    CartScreen()
}