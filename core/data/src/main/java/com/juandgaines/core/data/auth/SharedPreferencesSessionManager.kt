package com.juandgaines.core.data.auth

import android.content.SharedPreferences
import com.juandgaines.core.data.auth.refresh_token.RefreshTokenRequest
import com.juandgaines.core.data.network.safeCall
import com.juandgaines.core.domain.AuthData
import com.juandgaines.core.domain.auth.SessionManager
import com.juandgaines.core.domain.util.DataError.Network
import com.juandgaines.core.domain.util.Result
import com.juandgaines.core.domain.util.Result.Success
import com.juandgaines.core.domain.util.map
import com.juandgaines.core.domain.util.onError
import com.juandgaines.core.domain.util.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SharedPreferencesSessionManager(
    private val sharedPreferences: SharedPreferences,
    private val tokenApi: TokenApi
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

    override suspend fun refresh(): Result<AuthData?, Network> =
        withContext(Dispatchers.IO){
            val authData = get() ?: return@withContext Success(null)
            authData.let {
                safeCall {
                    tokenApi.refreshToken(
                        RefreshTokenRequest(
                            it.refreshToken,
                            it.userId
                        )
                    )
                }.map {
                    val newAuthData = AuthData(
                        accessToken = it.accessToken,
                        fullName = authData.fullName,
                        accessTokenExpirationTimestamp = it.expirationTimestamp,
                        refreshToken = authData.refreshToken,
                        userId = authData.userId
                    )
                    newAuthData
                }.onSuccess { data->
                    set(data)
                }.onError {
                    set(null)
                }
            }
        }

    override suspend fun checkAuth(): Result<Unit,Network> = withContext(Dispatchers.IO){
        safeCall {
            tokenApi.checkAuth()
        }
    }

    override suspend fun logout(): Result<Unit, Network> = withContext(Dispatchers.IO){
        safeCall {
            tokenApi.logout()
        }
    }

    companion object{
        const val KEY_AUTH_DATA = "KEY_AUTH_DATA"
    }
}