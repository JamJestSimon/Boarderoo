package pl.boarderoo.mobileapp.retrofit.data

data class RegisterRequest(
    val id: String,
    val email: String,
    val isVerified: Boolean,
    val address: String,
    val name: String,
    val password: String,
    val authorization: String,
    val surname: String,
    val token: String,
    val tokenCreationDate: String,
)
