package com.juandgaines.core.data.di

import com.juandgaines.core.data.auth.DefaultAuthCoreService
import com.juandgaines.core.data.auth.TokenApi
import com.juandgaines.core.domain.auth.AuthCoreService
import com.juandgaines.core.domain.auth.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AuthCoreServiceModule {

    @Provides
    @Singleton
    fun provideAuthCoreService(
        tokenApi: TokenApi,
        sessionManager: SessionManager
    ): AuthCoreService {
        return DefaultAuthCoreService(
            tokenApi = tokenApi,
            sessionManager = sessionManager
        )
    }
}