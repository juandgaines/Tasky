package com.juandgaines.core.data.network

import android.util.Log
import com.juandgaines.core.data.BuildConfig
import com.juandgaines.core.data.auth.TokenApi
import com.juandgaines.core.data.auth.refresh_token.RefreshTokenRequest
import com.juandgaines.core.domain.AuthData
import com.juandgaines.core.domain.auth.SessionManager
import com.juandgaines.core.domain.util.DataError.Network.UNAUTHORIZED
import com.juandgaines.core.domain.util.map
import com.juandgaines.core.domain.util.onError
import com.juandgaines.core.domain.util.onSuccess
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Provider

class AuthInterceptor @Inject constructor(
    private val sessionManager: SessionManager,
    private val tokenApiProvider: Provider<TokenApi>
): Interceptor {

    override fun intercept(chain: Chain): Response {
        val request = chain.request()


        val requestBuilder = chain.request().newBuilder()
            .addHeader("x-api-key", BuildConfig.API_KEY)
        if (
            request.url.toString().contains("authenticate") ||
            request.url.toString().contains("accessToken")
        ) {
            return chain.proceed(requestBuilder.build())
        }


        runBlocking {
            val tokenApi = tokenApiProvider.get()
            val authData = sessionManager.get()
            if (authData != null) {
                val isTokenExpired = sessionManager.isTokenExpired()
                if (isTokenExpired) {
                    safeCall {
                        tokenApi.checkAuth()
                    }.onSuccess {

                        requestBuilder.addHeader("Authorization", "Bearer $authData.accessToken")
                    }.onError { error->
                        Log.d("AuthInterceptor", "Token is Invalid")
                        when (error) {
                            UNAUTHORIZED -> {
                                Log.d("AuthInterceptor", "Refreshing....")
                                safeCall {
                                    tokenApi.refreshToken(
                                        RefreshTokenRequest(
                                            authData.refreshToken,
                                            authData.userId
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
                                }.onSuccess { newAuth ->
                                    Log.d("AuthInterceptor", "Refresh Ok")
                                    requestBuilder.addHeader(
                                        "Authorization", "Bearer ${newAuth.accessToken}"
                                    )
                                    sessionManager.set(newAuth)
                                }.onError {
                                    sessionManager.set(null)
                                }
                            }
                            else -> Unit
                        }
                    }
                }
                else {
                    requestBuilder.addHeader("Authorization", "Bearer ${authData.accessToken}")
                }
            }
        }
        return chain.proceed(requestBuilder.build())
    }
}