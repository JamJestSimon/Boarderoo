package pl.boarderoo.mobileapp.retrofit.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameModel(
    val image: List<String>,
    val id: String,
    val name: String,
    val type: String,
    val price: Float,
    val description: String,
    val publisher: String,
    @SerializedName("players_number") val playersNumber: String,
    val year: String,
    val rating: String,
    val enabled: Boolean,
    @SerializedName("available_copies") val availableCopies: Int
) : Parcelable
