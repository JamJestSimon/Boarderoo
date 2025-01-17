package pl.boarderoo.mobileapp.retrofit

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
        @Body userModel: UserModel
    ): Response<ResponseModel<String>>

    @GET("Game")
    suspend fun getGames(): Response<ResponseModel<List<GameModel>>>

    @POST("Order")
    suspend fun addOrder(
        @Body orderModel: OrderModel
    ): Response<ResponseModel<String>>

    @GET("Order")
    suspend fun getOrders(): Response<ResponseModel<List<OrderModel>>>

    @GET("Order/{id}")
    suspend fun getOrderById(
        @Path("id") id: String
    ): Response<ResponseModel<OrderModel>>

    //post register
    //TODO
}