package pl.boarderoo.mobileapp.retrofit.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderModel(
    val id: String,
    val start: String,
    val end: String,
    val status: String,
    val user: String,
    var paymentNumber: String,
    val items: List<String>,
    val price: Float
) : Parcelable
