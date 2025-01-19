package pl.boarderoo.mobileapp.retrofit.data

data class UpdatePasswordRequest(
    val email: String,
    val oldPassword: String,
    val newPassword: String
)
