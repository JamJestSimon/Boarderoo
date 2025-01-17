package pl.boarderoo.mobileapp.retrofit

import pl.boarderoo.mobileapp.retrofit.models.GameModel
import pl.boarderoo.mobileapp.retrofit.models.ResponseModel
import retrofit2.Response

interface GameServiceInterface {
    suspend fun getGames(): Response<ResponseModel<List<GameModel>>>
}

class GameService(
    private val apiService: APIService = getRetrofitClient()
) : GameServiceInterface {
    override suspend fun getGames(): Response<ResponseModel<List<GameModel>>> {
        return apiService.getGames()
    }
}