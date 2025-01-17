package pl.boarderoo.mobileapp.retrofit.models

import android.os.Parcelable
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
    val players_number: String,
    val year: String,
    val rating: Int,
    val enabled: Boolean,
    val available_copies: Int
) : Parcelable
