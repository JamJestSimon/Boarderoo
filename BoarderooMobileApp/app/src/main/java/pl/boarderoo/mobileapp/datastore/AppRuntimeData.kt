package pl.boarderoo.mobileapp.datastore

import pl.boarderoo.mobileapp.retrofit.models.GameModel
import pl.boarderoo.mobileapp.retrofit.models.UserModel

object AppRuntimeData {
    var user: UserModel? = null
    var cart: List<GameModel>? = null
}
