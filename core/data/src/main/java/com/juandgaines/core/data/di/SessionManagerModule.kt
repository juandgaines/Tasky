package com.juandgaines.core.data.di

import android.content.SharedPreferences
import com.juandgaines.core.data.auth.SharedPreferencesSessionManager
import com.juandgaines.core.data.auth.TokenApi
import com.juandgaines.core.data.network.AuthInterceptor
import com.juandgaines.core.domain.auth.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SessionManagerModule {

    @Provides
    @Singleton
    fun provideSessionManager(
        sharedPreferences: SharedPreferences,
        tokenApi: TokenApi
    ): SessionManager {
        return SharedPreferencesSessionManager(
            sharedPreferences = sharedPreferences,
            tokenApi = tokenApi
        )
    }

    @Provides
    @Singleton
    fun providesAuthInterceptor(
        sessionManager: SessionManager
    ): Interceptor = AuthInterceptor(sessionManager)
}