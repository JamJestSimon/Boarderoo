package pl.boarderoo.mobileapp.retrofit.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class OrderModel(
    val id: String,
    val start: Date,
    val end: Date,
    val status: String,
    val user: String,
    var paymentNumber: String,
    val items: List<String>,
    val price: Float
) : Parcelable
