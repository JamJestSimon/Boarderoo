package pl.boarderoo.mobileapp.retrofit.services

import pl.boarderoo.mobileapp.retrofit.APIService
import pl.boarderoo.mobileapp.retrofit.getRetrofitClient
import pl.boarderoo.mobileapp.retrofit.models.OrderModel
import pl.boarderoo.mobileapp.retrofit.models.ResponseModel
import pl.boarderoo.mobileapp.retrofit.models.UserModel
import retrofit2.Response

interface OrderServiceInterface {
    suspend fun getOrdersByUser(userModel: UserModel): Response<ResponseModel<List<OrderModel>>>
    suspend fun getOrderById(id: String): Response<ResponseModel<OrderModel>>
    suspend fun addOrder(orderModel: OrderModel): Response<ResponseModel<OrderModel>>
    suspend fun cancelOrder(orderModel: OrderModel): Response<ResponseModel<OrderModel>>
}

class OrderService(
    private val apiService: APIService = getRetrofitClient()
) : OrderServiceInterface {
    override suspend fun getOrdersByUser(userModel: UserModel): Response<ResponseModel<List<OrderModel>>> {
        return apiService.getOrdersByUserEmail(userModel.email)
    }

    override suspend fun getOrderById(id: String): Response<ResponseModel<OrderModel>> {
        return apiService.getOrderById(id)
    }

    override suspend fun addOrder(orderModel: OrderModel): Response<ResponseModel<OrderModel>> {
        return apiService.addOrder(orderModel)
    }

    override suspend fun cancelOrder(orderModel: OrderModel): Response<ResponseModel<OrderModel>> {
        return apiService.changeOrderStatus(orderModel.id, "Anulowane")
    }
}