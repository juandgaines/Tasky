package com.juandgaines.auth.data.network.di

import com.juandgaines.auth.data.network.AuthApi
import com.juandgaines.auth.data.network.RemoteAuthDataSourceImpl
import com.juandgaines.auth.domain.RemoteAuthDataSource
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


}