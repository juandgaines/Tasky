package com.juandgaines.core.data.di

import android.content.Context
import androidx.room.Room
import com.juandgaines.core.data.database.TaskyDataBase
import com.juandgaines.core.data.database.local.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomModule {

    @Provides
    @Singleton
    fun provideRoomDatabase(
        @ApplicationContext context: Context
    ): TaskyDataBase {
        return Room.databaseBuilder(
            context,
            TaskyDataBase::class.java,
            "tasky.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideTaskDao(
        taskyDataBase: TaskyDataBase
    ) :TaskDao = taskyDataBase.taskDao()
}


