package com.juandgaines.agenda.data.task.di

import com.juandgaines.agenda.data.task.DefaultTaskRepository
import com.juandgaines.agenda.data.task.remote.TaskApi
import com.juandgaines.agenda.domain.task.TaskRepository
import com.juandgaines.core.data.database.task.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import retrofit2.Retrofit
import javax.inject.Named
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
    fun provideTaskRepository(
        taskDao: TaskDao,
        taskApi: TaskApi,
        applicationScope:CoroutineScope
    ): TaskRepository {
        return DefaultTaskRepository(taskDao, taskApi, applicationScope)
    }
}