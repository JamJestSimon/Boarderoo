package pl.boarderoo.mobileapp.main

import android.content.Intent
import android.util.Log
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import kotlinx.coroutines.launch
import pl.boarderoo.mobileapp.LightButton
import pl.boarderoo.mobileapp.R
import pl.boarderoo.mobileapp.datastore.AppRuntimeData
import pl.boarderoo.mobileapp.retrofit.models.OrderModel
import pl.boarderoo.mobileapp.ui.theme.outlinedTextFieldColors
import pl.boarderoo.mobileapp.volley.PayPalVolley
import java.math.RoundingMode
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Date

data class CartItem(
    val name: String,
    val price: Double,
    val quantity: Int,
    val image: String
)

@Composable
fun CartScreen() {
    val context = LocalContext.current
    val activity = LocalActivity.current
    val scope = rememberCoroutineScope()

    val today = LocalDate.now()

    val months = (1..12).toList()
    val years = (today.year..2030).toList()

    // States for start date
    var startDay by remember { mutableStateOf(today.dayOfMonth) }
    var startMonth by remember { mutableStateOf(today.monthValue) }
    var startYear by remember { mutableStateOf(today.year) }

    // States for end date
    var endDay by remember { mutableStateOf(today.dayOfMonth) }
    var endMonth by remember { mutableStateOf(today.monthValue) }
    var endYear by remember { mutableStateOf(today.year) }

    // Helper function to get the number of days in a month
    fun getDaysInMonth(month: Int, year: Int): List<Int> {
        return (1..YearMonth.of(year, month).lengthOfMonth()).toList()
    }

    // Filtered options for the start date
    val startDays = getDaysInMonth(startMonth, startYear).filter {
        val date = LocalDate.of(startYear, startMonth, it)
        !date.isBefore(today)
    }
    val startMonths = months.filter { month ->
        val date = LocalDate.of(startYear, month, 1)
        !date.isBefore(today.withDayOfMonth(1))
    }
    val startYears = years

    // Filtered options for the end date
    val startDate by remember { derivedStateOf { LocalDate.of(startYear, startMonth, startDay) } }
    val filteredEndDays = getDaysInMonth(endMonth, endYear).filter {
        val endDate = LocalDate.of(endYear, endMonth, it)
        !endDate.isBefore(startDate)
    }
    val filteredEndMonths = months.filter { month ->
        val endDate = LocalDate.of(endYear, month, 1)
        !endDate.isBefore(startDate.withDayOfMonth(1))
    }
    val filteredEndYears = years.filter { year ->
        val endDate = LocalDate.of(year, 1, 1)
        !endDate.isBefore(startDate.withDayOfMonth(1))
    }

    val endDate by remember { derivedStateOf { LocalDate.of(endYear, endMonth, endDay) } }

    var cartItemList by remember { mutableStateOf(emptyList<CartItem>()) }


    LaunchedEffect(AppRuntimeData.cart) {
        cartItemList = AppRuntimeData.cart
            .groupingBy { it }
            .eachCount()
            .map { (game, count) ->
                // Processing game.image to the first image if it's in list format
                val imageUrl = game.image.toString()
                    .substringAfter("[")
                    .substringBefore("]")
                    .split(", ")
                    .firstOrNull()

                CartItem(
                    game.name,
                    game.price.toDouble(),
                    count,
                    imageUrl ?: ""
                )
            }
    }


    val orderItems by remember {
        derivedStateOf { AppRuntimeData.cart.map { it.name } }
    }

    val totalPrice by remember {
        derivedStateOf {
            cartItemList.sumOf {
                it.quantity * it.price * (ChronoUnit.DAYS.between(
                    startDate,
                    endDate
                ) + 1)
            }
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        if (AppRuntimeData.cart.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Text("Koszyk pusty", modifier = Modifier.align(Alignment.Center))
            }
        } else {
            Text("Data rozpoczęcia zamówienia")
            Row {
                DropdownDateSelector(
                    options = startDays,
                    selectedValue = startDay
                ) {
                    startDay = it
                    if (startDate > endDate) {
                        endDay = startDay
                        endMonth = startMonth
                        endYear = startYear
                    }
                }
                DropdownDateSelector(
                    options = startMonths,
                    selectedValue = startMonth
                ) {
                    startMonth = it
                    if (startDate > endDate) {
                        endDay = startDay
                        endMonth = startMonth
                        endYear = startYear
                    }
                    if (startDay > getDaysInMonth(startMonth, startYear).last()) {
                        startDay = getDaysInMonth(startMonth, startYear).last()
                    }
                }
                DropdownDateSelector(
                    options = startYears,
                    selectedValue = startYear
                ) {
                    startYear = it
                    if (startDate > endDate) {
                        endDay = startDay
                        endMonth = startMonth
                        endYear = startYear
                    }
                    if (startDay > getDaysInMonth(startMonth, startYear).last()) {
                        startDay = getDaysInMonth(startMonth, startYear).last()
                    }
                }
            }
            Text("Data zakończenia zamówienia")
            Row {
                DropdownDateSelector(
                    options = filteredEndDays,
                    selectedValue = endDay
                ) {
                    endDay = it
                }
                DropdownDateSelector(
                    options = filteredEndMonths,
                    selectedValue = endMonth
                ) {
                    endMonth = it
                    if (endDay > getDaysInMonth(endMonth, endYear).last()) {
                        endDay = getDaysInMonth(endMonth, endYear).last()
                    }
                }
                DropdownDateSelector(
                    options = filteredEndYears,
                    selectedValue = endYear
                ) {
                    endYear = it
                    if (endDay > getDaysInMonth(endMonth, endYear).last()) {
                        endDay = getDaysInMonth(endMonth, endYear).last()
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text("Zawartość zamówienia")
            Spacer(modifier = Modifier.height(10.dp))
            LazyColumn(
                modifier = Modifier.height(370.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                itemsIndexed(cartItemList) { index, item ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .clip(RoundedCornerShape(25.dp))
                            .background(Color(0xFFBF1331))


                    ) {
                        var offsetX by remember { mutableStateOf(0f) }
                        val dismissThreshold = 100f // Ustalamy próg do wykrywania gestu (w pikselach)
                        var isItemRemoved by remember { mutableStateOf(true) }
                        var currentItem: String = ""
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .offset(x = offsetX.dp)
                                .clip(RoundedCornerShape(25.dp))
                                .background(color = colorResource(R.color.buttonSecondColor))
                                .padding(10.dp)
                                .pointerInput(Unit) {
                                    detectHorizontalDragGestures { _, dragAmount ->
                                        if (!isItemRemoved) {
                                            val speedFactor = 0.2f
                                            offsetX += dragAmount * speedFactor

                                            val maxOffsetX = 0f
                                            val minOffsetX = -dismissThreshold

                                            if (offsetX > maxOffsetX) {
                                                offsetX = maxOffsetX
                                            }
                                            if (offsetX < minOffsetX) {
                                                Log.e("TAG", currentItem)

                                                AppRuntimeData.cart.removeAt(index)

                                                cartItemList = cartItemList.filterIndexed { i, _ -> i != index }
                                                isItemRemoved = true

                                                offsetX = 0f
                                            }
                                        }
                                    }
                                }
                                .pointerInput(Unit) {
                                    detectTapGestures(
                                        onPress = {
                                            currentItem = item.name
                                            Log.e("TAG", "KLIKAM " + item.name + " numer " + index)
                                            // Resetujemy stan, żeby umożliwić nowy gest
                                            if (isItemRemoved) {
                                                isItemRemoved = false
                                                offsetX = 0f // Resetujemy offset
                                            }
                                        }
                                    )
                                }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.Start

                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(100.dp)
                                        .padding(end = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = rememberImagePainter("https://firebasestorage.googleapis.com/v0/b/boarderoo-71469.firebasestorage.app/o/files%2F${item.image}?alt=media"),
                                        contentDescription = "Game Image",
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(RoundedCornerShape(15.dp))
                                            .background(Color.White)
                                            .border(4.dp, Color(0xFF422D29), RoundedCornerShape(15.dp))
                                    )
                                }

                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Przedmiot: ${item.name}")
                                    Text("Ilość: ${item.quantity}")
                                    Text(
                                        "Koszt: ${
                                            (item.price * item.quantity * (ChronoUnit.DAYS.between(
                                                startDate, endDate // Zmieniono dla przykładu
                                            ) + 1)).toBigDecimal().setScale(2, RoundingMode.HALF_EVEN)
                                                .toDouble()
                                        } zł"
                                    )
                                }
                            }
                        }
                    }

                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
            Text(
                text = "Całkowita wartość zamówienia: ${
                    totalPrice.toBigDecimal().setScale(2, RoundingMode.HALF_EVEN).toDouble()
                } zł",
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(10.dp))
            LightButton(
                onClick = {

                    AppRuntimeData.order = OrderModel(
                        "",
                        DateTimeFormatter.ISO_INSTANT.format(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                        DateTimeFormatter.ISO_INSTANT.format(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                        "Zapłacone",
                        AppRuntimeData.user!!.email,
                        "",
                        orderItems,
                        totalPrice.toBigDecimal().setScale(2, RoundingMode.HALF_EVEN).toFloat()
                    )
                    PayPalVolley.getOrderId(
                        onSuccess = {
                            AppRuntimeData.orderId = it
                            context.startActivity(
                                Intent(context, CheckoutFragment::class.java)
                            )
                            activity?.finish()
                            AppRuntimeData.cart.clear()
                        },
                        onError = {
                            //TODO: handle error
                        }
                    )
                },
                text = "Przejdź do płatności",
                fontSize = 12.sp
            )
        }}
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownDateSelector(options: List<Int>, selectedValue: Int, onSelect: (Int) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        OutlinedTextField(
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryEditable, true)
                .width(120.dp),
            value = selectedValue.toString(),
            onValueChange = { },
            readOnly = true,
            colors = outlinedTextFieldColors(),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            singleLine = true
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { item ->
                DropdownMenuItem(
                    onClick = {
                        onSelect(item)
                        expanded = false
                    },
                    text = {
                        Text(text = item.toString())
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun CartScreenPreview() {
    CartScreen()
}