package com.juandgaines.core.data.di

import android.content.Context
import com.juandgaines.core.data.network.AndroidConnectivityObserver
import com.juandgaines.core.domain.network.ConnectivityObserver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ConnectivityModule {

    @Provides
    @Singleton
    fun provideConnectivityObserver(
        @ApplicationContext
        context: Context,
    ): ConnectivityObserver {
        return AndroidConnectivityObserver(context)
    }
}

