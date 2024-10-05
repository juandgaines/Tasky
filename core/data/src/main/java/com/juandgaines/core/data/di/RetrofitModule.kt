package com.juandgaines.core.data.di

import com.juandgaines.core.data.BuildConfig
import com.juandgaines.core.data.auth.TokenApi
import com.juandgaines.core.domain.auth.SessionManager
import com.juandgaines.core.domain.util.DataError.Network.UNAUTHORIZED
import com.juandgaines.core.domain.util.onError
import com.juandgaines.core.domain.util.onSuccess
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RetrofitModule {

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(
                Json.asConverterFactory( "application/json; charset=UTF8".toMediaType())
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG)
                HttpLoggingInterceptor.Level.BODY
            else
                HttpLoggingInterceptor.Level.NONE
        }
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(
        sessionManager: SessionManager
    ): Interceptor = Interceptor { chain ->
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
        chain.proceed(requestBuilder.build())
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: Interceptor
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
        return builder.build()
    }

    @Provides
    @Singleton
    fun provideTokenApi(retrofit: Retrofit): TokenApi {
        return retrofit.create(TokenApi::class.java)
    }
}