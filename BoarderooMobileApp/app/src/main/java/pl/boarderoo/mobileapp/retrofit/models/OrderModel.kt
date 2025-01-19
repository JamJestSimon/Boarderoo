package pl.boarderoo.mobileapp.retrofit.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class OrderModel(
    val id: String,
    //TODO - odkomentować jak będzie działało
    //val start: Date,
    //val end: Date,
    val status: Int,
    val user: String,
    val items: List<String>,
    val price: Float
) : Parcelable
