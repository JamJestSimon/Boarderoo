package pl.boarderoo.mobileapp.retrofit.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pl.boarderoo.mobileapp.datastore.AppRuntimeData
import pl.boarderoo.mobileapp.retrofit.models.OrderModel
import pl.boarderoo.mobileapp.retrofit.services.OrderService

class OrderListViewModel(
    private val service: OrderService = OrderService()
) : ViewModel() {
    private val _orderList = MutableStateFlow<List<OrderModel>?>(null)
    private val _errorMessage = MutableStateFlow<String?>(null)
    private val _isLoading = MutableStateFlow(true)

    val orderList: StateFlow<List<OrderModel>?> get() = _orderList.asStateFlow()
    val errorMessage: StateFlow<String?> get() = _errorMessage.asStateFlow()
    val isLoading: StateFlow<Boolean> get() = _isLoading.asStateFlow()

    fun getOrderList() {
        viewModelScope.launch {
            _isLoading.value = true
            val response = service.getOrdersByUser(AppRuntimeData.user!!)
            if(response.isSuccessful) {
                val body = response.body()
                if(body != null) {
                    _isLoading.value = false
                    _orderList.value = body.data
                }
            } else {
                val error = response.errorBody()
                if(error != null) {
                    _isLoading.value = false
                    _errorMessage.value = error.toString()
                }
            }
        }
    }
}