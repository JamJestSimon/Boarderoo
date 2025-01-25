package pl.boarderoo.mobileapp.retrofit.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    val email: String,
    val isVerified: Boolean,
    val address: String,
    val name: String,
    val password: String,
    val surname: String,
    val token: String
) : Parcelable
