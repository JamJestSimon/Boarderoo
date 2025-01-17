package pl.boarderoo.mobileapp.retrofit

import pl.boarderoo.mobileapp.retrofit.models.ResponseModel
import retrofit2.Response

interface LoginServiceInterface {
    suspend fun getLoginResult(email: String, password: String): Response<ResponseModel<String>>
}

class LoginService(
    private val apiService: APIService = getRetrofitClient()
): LoginServiceInterface {
    override suspend fun getLoginResult(email: String, password: String): Response<ResponseModel<String>> {
        return apiService.getLoginResult(LoginRequest(email, password))
    }
}