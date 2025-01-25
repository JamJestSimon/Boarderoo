package pl.boarderoo.mobileapp.retrofit.services

import pl.boarderoo.mobileapp.retrofit.APIService
import pl.boarderoo.mobileapp.retrofit.data.LoginRequest
import pl.boarderoo.mobileapp.retrofit.data.RegisterRequest
import pl.boarderoo.mobileapp.retrofit.getRetrofitClient
import pl.boarderoo.mobileapp.retrofit.models.ResponseModel
import retrofit2.Response

interface RegisterServiceInterface {
    suspend fun getRegisterResult(
        id: String,
        email: String,
        isVerified: Boolean,
        address: String,
        name: String,
        password: String,
        authorization: String,
        surname: String,
        token: String,
        tokenCreationDate: String
    ): Response<ResponseModel<String>>
}

class RegisterService(
    private val apiService: APIService = getRetrofitClient()
) : RegisterServiceInterface {
    override suspend fun getRegisterResult(
        id: String,
        email: String,
        isVerified: Boolean,
        address: String,
        name: String,
        password: String,
        authorization: String,
        surname: String,
        token: String,
        tokenCreationDate: String
    ): Response<ResponseModel<String>> {
        return apiService.registerUser(
            RegisterRequest(
                id = id,
                email = email,
                isVerified = isVerified,
                address = address,
                name = name,
                password = password,
                authorization = authorization,
                surname = surname,
                token = token,
                tokenCreationDate = tokenCreationDate
            )
        )
    }
}