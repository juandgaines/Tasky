package com.juandgaines.agenda.data.task.di

import com.juandgaines.agenda.data.task.local.RoomLocalTaskDataSource
import com.juandgaines.agenda.data.task.remote.RetrofitRemoteTaskDataSource
import com.juandgaines.agenda.data.task.remote.TaskApi
import com.juandgaines.agenda.domain.task.LocalTaskDataSource
import com.juandgaines.agenda.domain.task.RemoteTaskDataSource
import com.juandgaines.core.data.database.task.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class TaskModule {
    @Provides
    @Singleton
    fun provideTaskApi(
        retrofit: Retrofit
    ): TaskApi {
        return retrofit.create(TaskApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRoomLocalTaskDataSource(
        taskDao: TaskDao
    ): LocalTaskDataSource {
        return RoomLocalTaskDataSource(taskDao)
    }

    @Provides
    @Singleton
    fun provideRetrofitRemoteTaskDataSource(
        taskApi: TaskApi
    ): RemoteTaskDataSource {
        return RetrofitRemoteTaskDataSource(taskApi)
    }

}