package pl.boarderoo.mobileapp.retrofit.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pl.boarderoo.mobileapp.retrofit.models.ResponseModel
import pl.boarderoo.mobileapp.retrofit.models.UserModel
import pl.boarderoo.mobileapp.retrofit.services.UserService

class UserDetailsViewModel(
    private val service: UserService = UserService()
) : ViewModel() {
    private val _userDetails = MutableStateFlow<UserModel?>(null)
    private val _errorMessage = MutableStateFlow<String?>(null)
    private val _isLoading = MutableStateFlow(true)

    val user: StateFlow<UserModel?> get() = _userDetails.asStateFlow()
    val errorMessage: StateFlow<String?> get() = _errorMessage.asStateFlow()
    val isLoading: StateFlow<Boolean> get() = _isLoading.asStateFlow()

    val gson = Gson()

    fun getUserDetails(email: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val response = service.getUserByEmail(email)
            if(response.isSuccessful) {
                val body = response.body()
                if(body != null) {
                    _isLoading.value = false
                    _userDetails.value = body.data
                }
            } else {
                val error = response.errorBody()
                if(error != null) {
                    _isLoading.value = false
                    _errorMessage.value = gson.fromJson(error.string(), ResponseModel::class.java).message
                }
            }
        }
    }
}