package pl.boarderoo.mobileapp.retrofit.models

data class ResponseModel<T>(
    val message: String,
    val resultCode: Int,
    val data: T
)
