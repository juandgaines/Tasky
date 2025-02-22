package com.juandgaines.core.data.di

import android.content.Context
import androidx.room.Room
import com.juandgaines.core.data.database.TaskyDataBase
import com.juandgaines.core.data.database.agenda.AgendaSyncDao
import com.juandgaines.core.data.database.event.EventDao
import com.juandgaines.core.data.database.reminder.ReminderDao
import com.juandgaines.core.data.database.task.TaskDao
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
        @ApplicationContext context: Context,
    ): TaskyDataBase {
        return Room.databaseBuilder(
            context,
            TaskyDataBase::class.java,
            "tasky.db"
        ).build()
    }
}

@Module
@InstallIn(SingletonComponent::class)
class DaoModule {

    @Provides
    @Singleton
    fun provideTaskDao(
        taskyDataBase: TaskyDataBase
    ) :TaskDao = taskyDataBase.taskDao()

    @Provides
    @Singleton
    fun provideReminderDao(
        taskyDataBase: TaskyDataBase
    ) : ReminderDao = taskyDataBase.reminderDao()

    @Provides
    @Singleton
    fun provideAgendaSyncDao(
        taskyDataBase: TaskyDataBase
    ) : AgendaSyncDao = taskyDataBase.agendaSyncDao()

    @Provides
    @Singleton
    fun provideEventDao(
        taskyDataBase: TaskyDataBase
    ) : EventDao = taskyDataBase.eventDao()
}


