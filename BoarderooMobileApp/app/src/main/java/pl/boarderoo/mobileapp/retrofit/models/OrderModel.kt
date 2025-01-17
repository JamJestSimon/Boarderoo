package pl.boarderoo.mobileapp.retrofit.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class OrderModel(
    val id: String,
    val start: Date,
    val end: Date,
    val status: StatusType,
    val user: UserModel,
    val items: List<GameModel>,
    val price: Float
) : Parcelable
