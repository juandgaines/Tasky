package com.juandgaines.agenda.data.task.di

import com.juandgaines.agenda.data.task.remote.TaskApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class TaskModule {
    @Provides
    fun provideTaskApi(
        retrofit: Retrofit
    ): TaskApi {
        return retrofit.create(TaskApi::class.java)
    }
}