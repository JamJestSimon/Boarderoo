package pl.boarderoo.mobileapp.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

suspend fun saveLoginData(context: Context, isLoggedIn: Boolean, email: String) {
    context.dataStore.edit { preferences ->
        preferences[PreferencesKeys.IS_LOGGED_IN] = isLoggedIn
        preferences[PreferencesKeys.USER_EMAIL] = email
    }
}

fun getLoginState(context: Context): Flow<Boolean> {
    return context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.IS_LOGGED_IN] ?: false
    }
}

fun getUserEmail(context: Context): Flow<String?> {
    return context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.USER_EMAIL]
    }
}