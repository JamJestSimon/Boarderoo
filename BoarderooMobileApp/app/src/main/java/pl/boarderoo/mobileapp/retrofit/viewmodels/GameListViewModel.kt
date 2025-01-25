package pl.boarderoo.mobileapp.retrofit.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pl.boarderoo.mobileapp.retrofit.models.GameModel
import pl.boarderoo.mobileapp.retrofit.models.ResponseModel
import pl.boarderoo.mobileapp.retrofit.services.GameService

class GameListViewModel(
    private val service: GameService = GameService()
) : ViewModel() {
    private val _gameList = MutableStateFlow<List<GameModel>?>(null)
    private val _errorMessage = MutableStateFlow<String?>(null)
    private val _isLoading = MutableStateFlow(true)

    val gameList: StateFlow<List<GameModel>?> get() = _gameList.asStateFlow()
    val errorMessage: StateFlow<String?> get() = _errorMessage.asStateFlow()
    val isLoading: StateFlow<Boolean> get() = _isLoading.asStateFlow()

    val gson = Gson()

    fun getGameList() {
        viewModelScope.launch {
            _isLoading.value = true
            val response = service.getGames()
            if(response.isSuccessful) {
                val body = response.body()
                if(body != null) {
                    _isLoading.value = false
                    _gameList.value = body.data
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