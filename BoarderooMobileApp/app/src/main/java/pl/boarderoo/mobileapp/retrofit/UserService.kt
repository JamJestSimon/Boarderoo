package pl.boarderoo.mobileapp.retrofit

import pl.boarderoo.mobileapp.retrofit.models.ResponseModel
import pl.boarderoo.mobileapp.retrofit.models.UserModel
import retrofit2.Response

interface UserServiceInterface {
    suspend fun getUserByEmail(email: String): Response<ResponseModel<UserModel>>
}

class UserService(
    private val apiService: APIService = getRetrofitClient()
) : UserServiceInterface {
    override suspend fun getUserByEmail(email: String): Response<ResponseModel<UserModel>> {
        return apiService.getUserByEmail(email)
    }
}