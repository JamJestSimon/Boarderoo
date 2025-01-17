package pl.boarderoo.mobileapp.retrofit

import pl.boarderoo.mobileapp.retrofit.models.LoginModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {
    @GET("Login")
    suspend fun getLoginResult(
        @Query("email") email: String,
        @Query("password") password: String,
    ): Response<LoginModel>
}