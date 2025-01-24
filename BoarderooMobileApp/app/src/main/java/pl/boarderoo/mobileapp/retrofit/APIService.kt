package pl.boarderoo.mobileapp.retrofit

import pl.boarderoo.mobileapp.retrofit.data.LoginRequest
import pl.boarderoo.mobileapp.retrofit.data.UpdatePasswordRequest
import pl.boarderoo.mobileapp.retrofit.models.GameModel
import pl.boarderoo.mobileapp.retrofit.models.OrderModel
import pl.boarderoo.mobileapp.retrofit.models.ResponseModel
import pl.boarderoo.mobileapp.retrofit.models.UserModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
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

    @PUT("User")
    suspend fun editUser(
        @Query("email") email: String,
        @Query("name") name: String?,
        @Query("surname") surname: String?,
        @Query("address") address: String?
    ): Response<ResponseModel<UserModel>>

    @PUT("User/password")
    suspend fun changePassword(
        @Body request: UpdatePasswordRequest
    ): Response<ResponseModel<String>>

    @GET("Game")
    suspend fun getGames(): Response<ResponseModel<List<GameModel>>>

    @GET("Game/{id}")
    suspend fun getGameById(
        @Path("id") id: String
    ): Response<ResponseModel<GameModel>>

    @POST("Order")
    suspend fun addOrder(
        @Body orderModel: OrderModel
    ): Response<ResponseModel<OrderModel>>

    @GET("Order/user/{email}")
    suspend fun getOrdersByUserEmail(
        @Path("email") email: String
    ): Response<ResponseModel<List<OrderModel>>>

    @GET("Order/{id}")
    suspend fun getOrderById(
        @Path("id") id: String
    ): Response<ResponseModel<OrderModel>>

    @PUT("Order")
    suspend fun changeOrderStatus(
        @Query("id") id: String,
        @Query("status") status: String
    ): Response<ResponseModel<OrderModel>>

    //post register
    //TODO
}