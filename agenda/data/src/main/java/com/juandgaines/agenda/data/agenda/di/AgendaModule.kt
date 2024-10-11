package com.juandgaines.agenda.data.agenda.di

import com.juandgaines.agenda.data.agenda.remote.AgendaApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AgendaModule {
    @Provides
    @Singleton
    fun provideAgendaApi(retrofit: Retrofit): AgendaApi {
        return retrofit.create(AgendaApi::class.java)
    }
}