package com.juandgaines.core.data.di

import android.content.SharedPreferences
import com.juandgaines.core.data.auth.SharedPreferencesSessionManager
import com.juandgaines.core.data.auth.TokenApi
import com.juandgaines.core.data.auth.refresh_token.RemoteTokenDataSourceImpl
import com.juandgaines.core.domain.auth.SessionManager
import com.juandgaines.core.domain.auth.refresh_token.RemoteTokenDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SessionManagerModule {

    @Provides
    @Singleton
    fun provideSessionManager(
        sharedPreferences: SharedPreferences,
    ): SessionManager {
        return SharedPreferencesSessionManager(
            sharedPreferences = sharedPreferences,
        )
    }

    @Provides
    @Singleton
    fun provideTokenApi(
        retrofit: Retrofit
    ): TokenApi {
        return retrofit.create(TokenApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRemoteTokenDataSource(
        tokenApi: TokenApi
    ): RemoteTokenDataSource {
        return RemoteTokenDataSourceImpl(tokenApi)
    }

}