package pl.boarderoo.mobileapp.retrofit

import pl.boarderoo.mobileapp.retrofit.models.OrderModel
import pl.boarderoo.mobileapp.retrofit.models.ResponseModel
import retrofit2.Response

interface OrderServiceInterface {
    suspend fun getOrders(): Response<ResponseModel<List<OrderModel>>>
    suspend fun getOrderById(id: String): Response<ResponseModel<OrderModel>>
    suspend fun addOrder(orderModel: OrderModel): Response<ResponseModel<String>>
}

class OrderService(
    private val apiService: APIService = getRetrofitClient()
) : OrderServiceInterface {
    override suspend fun getOrders(): Response<ResponseModel<List<OrderModel>>> {
        return apiService.getOrders()
    }

    override suspend fun getOrderById(id: String): Response<ResponseModel<OrderModel>> {
        return apiService.getOrderById(id)
    }

    override suspend fun addOrder(orderModel: OrderModel): Response<ResponseModel<String>> {
        return apiService.addOrder(orderModel)
    }
}