package pl.boarderoo.mobileapp.retrofit

import pl.boarderoo.mobileapp.retrofit.models.LoginModel
import retrofit2.Response

interface LoginServiceInterface {
    suspend fun getLoginResult(email: String, password: String): Response<LoginModel>
}

class LoginService(
    private val apiService: APIService = getRetrofitClient()
): LoginServiceInterface {
    override suspend fun getLoginResult(email: String, password: String): Response<LoginModel> {
        return apiService.getLoginResult(email, password)
    }
}