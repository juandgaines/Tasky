package com.juandgaines.core.data.auth

import android.content.SharedPreferences
import com.juandgaines.core.domain.AuthData
import com.juandgaines.core.domain.auth.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class SharedPreferencesSessionManager @Inject constructor(
    private val sharedPreferences: SharedPreferences,
):SessionManager {

    override suspend fun get(): AuthData? {
        return withContext(Dispatchers.IO){
            val json = sharedPreferences.getString(KEY_AUTH_DATA, null)
            json?.let {
                Json.decodeFromString<AuthDataDto>(it).toAuthData()
            }
        }
    }

    override suspend fun set(data: AuthData?) {
        withContext(Dispatchers.IO){
            if (data == null){
                sharedPreferences.edit()
                    .remove(
                        KEY_AUTH_DATA
                    ).apply()

                return@withContext
            }
            val json = Json.encodeToString(data.toAuthDataDto())
            sharedPreferences.edit()
                .putString(KEY_AUTH_DATA,json)
                .apply()
        }
    }

    override suspend fun getToken(): String {
        return withContext(Dispatchers.IO){
            get()?.accessToken ?: ""
        }
    }

    override suspend fun isTokenExpired(): Boolean {
        val authData = get() ?: return false
        return System.currentTimeMillis() >= authData.accessTokenExpirationTimestamp
    }


    companion object{
        const val KEY_AUTH_DATA = "KEY_AUTH_DATA"
    }
}