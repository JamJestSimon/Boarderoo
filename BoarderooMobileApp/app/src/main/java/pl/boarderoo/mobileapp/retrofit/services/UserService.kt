package pl.boarderoo.mobileapp.retrofit.services

import pl.boarderoo.mobileapp.retrofit.APIService
import pl.boarderoo.mobileapp.retrofit.data.UpdatePasswordRequest
import pl.boarderoo.mobileapp.retrofit.getRetrofitClient
import pl.boarderoo.mobileapp.retrofit.models.ResponseModel
import pl.boarderoo.mobileapp.retrofit.models.UserModel
import retrofit2.Response

interface UserServiceInterface {
    suspend fun getUserByEmail(email: String): Response<ResponseModel<UserModel>>
    suspend fun editUser(email: String, name: String?, surname: String?, address: String?): Response<ResponseModel<UserModel>>
    suspend fun updatePassword(email: String, oldPassword: String, newPassword: String): Response<ResponseModel<String>>
}

class UserService(
    private val apiService: APIService = getRetrofitClient()
) : UserServiceInterface {
    override suspend fun getUserByEmail(email: String): Response<ResponseModel<UserModel>> {
        return apiService.getUserByEmail(email)
    }

    override suspend fun editUser(email: String, name: String?, surname: String?, address: String?): Response<ResponseModel<UserModel>> {
        return apiService.editUser(email, name, surname, address)
    }

    override suspend fun updatePassword(email: String, oldPassword: String, newPassword: String): Response<ResponseModel<String>> {
        return apiService.changePassword(UpdatePasswordRequest(email, oldPassword, newPassword))
    }
}