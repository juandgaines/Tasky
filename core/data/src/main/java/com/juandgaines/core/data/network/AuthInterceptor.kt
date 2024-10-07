package com.juandgaines.core.data.network

import com.juandgaines.core.data.BuildConfig
import com.juandgaines.core.domain.auth.SessionManager
import com.juandgaines.core.domain.util.DataError.Network.UNAUTHORIZED
import com.juandgaines.core.domain.util.onError
import com.juandgaines.core.domain.util.onSuccess
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val sessionManager: SessionManager
): Interceptor {
    override fun intercept(chain: Chain): Response {
        val requestBuilder = chain.request().newBuilder()
            .addHeader("x-api-key", BuildConfig.API_KEY)
        runBlocking {
            val authData = sessionManager.get()
            if (authData != null) {
                val shouldValidateToken = authData.accessTokenExpirationTimestamp - System.currentTimeMillis() < 0

                if (shouldValidateToken) {
                    sessionManager
                        .checkAuth()
                        .onSuccess {
                            requestBuilder.addHeader("Authorization", "Bearer $authData.accessToken")
                        }
                        .onError { error->
                            when (error) {
                                UNAUTHORIZED -> {
                                    sessionManager
                                        .refresh()
                                        .onSuccess { tokenRefreshed ->
                                            tokenRefreshed?.let {
                                                requestBuilder.addHeader(
                                                    "Authorization", "Bearer ${it.accessToken}"
                                                )
                                            }?: run {
                                                sessionManager.set(null)
                                            }
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