package pl.boarderoo.mobileapp.retrofit

import pl.boarderoo.mobileapp.retrofit.models.ResponseModel
import pl.boarderoo.mobileapp.retrofit.models.UserModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService {
    @POST("Login")
    suspend fun getLoginResult(
        @Body request: LoginRequest
    ): Response<ResponseModel<String>>

    @GET("User/{email}")
    suspend fun getUserByEmail(
        @Path("email") email: String
    ): Response<ResponseModel<UserModel>>
}