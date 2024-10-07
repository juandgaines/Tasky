package com.juandgaines.auth.data.network.di

import com.juandgaines.auth.data.DefaultAuthRepository
import com.juandgaines.auth.data.network.AuthApi
import com.juandgaines.auth.data.network.RemoteAuthDataSourceImpl
import com.juandgaines.auth.domain.AuthRepository
import com.juandgaines.auth.domain.RemoteAuthDataSource
import com.juandgaines.core.domain.auth.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class AuthModule {

    @Provides
    fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    fun provideRemoteAuthDataSource(authApi: AuthApi): RemoteAuthDataSource {
        return RemoteAuthDataSourceImpl(authApi)
    }

    @Provides
    fun provideAuthRepository(
        remoteAuthDataSource: RemoteAuthDataSource,
        sessionStorage: SessionManager
    ): AuthRepository {
        return DefaultAuthRepository(remoteAuthDataSource, sessionStorage)
    }

}