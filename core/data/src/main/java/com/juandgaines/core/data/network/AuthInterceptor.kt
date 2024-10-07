package com.juandgaines.core.data.network

import com.juandgaines.core.data.BuildConfig
import com.juandgaines.core.domain.auth.SessionManager
import com.juandgaines.core.domain.auth.refresh_token.RemoteTokenDataSource
import com.juandgaines.core.domain.util.DataError.Network.UNAUTHORIZED
import com.juandgaines.core.domain.util.onError
import com.juandgaines.core.domain.util.onSuccess
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val sessionManager: SessionManager,
    private val remoteTokenDataSource: RemoteTokenDataSource
): Interceptor {
    override fun intercept(chain: Chain): Response {
        val requestBuilder = chain.request().newBuilder()
            .addHeader("x-api-key", BuildConfig.API_KEY)
        runBlocking {
            val authData = sessionManager.get()
            if (authData != null) {
                val isTokenExpired = sessionManager.isTokenExpired()

                if (isTokenExpired) {
                    remoteTokenDataSource
                        .checkAuth()
                        .onSuccess {
                            requestBuilder.addHeader("Authorization", "Bearer $authData.accessToken")
                        }
                        .onError { error->
                            when (error) {
                                UNAUTHORIZED -> {
                                    remoteTokenDataSource
                                        .refreshToken(
                                            authData
                                        )
                                        .onSuccess { newAuth ->

                                            remoteTokenDataSource.refreshToken(
                                                newAuth
                                            ).onSuccess { data->
                                                sessionManager.set(data)
                                            }.onError {
                                                sessionManager.set(null)
                                            }
                                            requestBuilder.addHeader(
                                                "Authorization", "Bearer ${newAuth.accessToken}"
                                            )
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