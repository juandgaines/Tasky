package com.juandgaines.core.data.auth

import android.content.SharedPreferences
import com.juandgaines.core.data.network.safeCall
import com.juandgaines.core.domain.AuthData
import com.juandgaines.core.domain.auth.SessionManager
import com.juandgaines.core.domain.auth.SessionManager.CheckAuthType
import com.juandgaines.core.domain.util.DataError.Network.UNAUTHORIZED
import com.juandgaines.core.domain.util.Result.Error
import com.juandgaines.core.domain.util.Result.Success
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SessionManagerImpl(
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

    override suspend fun refresh(): AuthData? {
        TODO("Not yet implemented")
    }

    override suspend fun checkAuth(): CheckAuthType {
        val response = safeCall {
            tokenApi.checkAuth()
        }

        return when (response){
            is Success -> {
                CheckAuthType.Valid
            }

            is Error -> {
                when(response.error){
                    UNAUTHORIZED -> CheckAuthType.Invalid
                    else -> CheckAuthType.Error
                }
            }
        }
    }

    companion object{
        const val KEY_AUTH_DATA = "KEY_AUTH_DATA"
    }
}