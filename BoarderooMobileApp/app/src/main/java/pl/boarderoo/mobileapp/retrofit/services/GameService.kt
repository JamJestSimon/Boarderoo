package pl.boarderoo.mobileapp.retrofit.services

import pl.boarderoo.mobileapp.retrofit.APIService
import pl.boarderoo.mobileapp.retrofit.getRetrofitClient
import pl.boarderoo.mobileapp.retrofit.models.GameModel
import pl.boarderoo.mobileapp.retrofit.models.ResponseModel
import retrofit2.Response

interface GameServiceInterface {
    suspend fun getGames(): Response<ResponseModel<List<GameModel>>>
    suspend fun getGameById(id: String): Response<ResponseModel<GameModel>>
}

class GameService(
    private val apiService: APIService = getRetrofitClient()
) : GameServiceInterface {
    override suspend fun getGames(): Response<ResponseModel<List<GameModel>>> {
        return apiService.getGames()
    }

    override suspend fun getGameById(id: String): Response<ResponseModel<GameModel>> {
        return apiService.getGameById(id)
    }
}