package com.juandgaines.core.data.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.juandgaines.core.data.database.TaskyDataBase
import com.juandgaines.core.data.database.reminder.ReminderDao
import com.juandgaines.core.data.database.reminder.ReminderEntity
import com.juandgaines.core.data.database.task.TaskDao
import com.juandgaines.core.data.database.task.TaskEntity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomModule {

    @Provides
    @Singleton
    fun provideRoomDatabase(
        @ApplicationContext context: Context,
        taskDao: Provider<TaskDao>,
        reminderDao: Provider<ReminderDao>,
        applicationScope: CoroutineScope
    ): TaskyDataBase {
        return Room.databaseBuilder(
            context,
            TaskyDataBase::class.java,
            "tasky.db"
        ).addCallback(
            object:RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    applicationScope.launch {
                        //TODO: Remove this
                        taskDao.get().upsertTasks(
                            listOf(
                                TaskEntity(
                                    id = "1",
                                    title = "Title 1",
                                    description = "Description 1",
                                    time = ZonedDateTime.now().toEpochSecond().times(1000),
                                    isDone = false,
                                    remindAt = ZonedDateTime.now().toEpochSecond().times(1000)
                                ),
                                TaskEntity(
                                    id = "2",
                                    title = "Title 2",
                                    description = "Description 2",
                                    time = ZonedDateTime.now().plusHours(1).toEpochSecond().times(1000),
                                    isDone = false,
                                    remindAt = ZonedDateTime.now().toEpochSecond().times(1000)
                                ),
                                TaskEntity(
                                    id = "3",
                                    title = "Title 3",
                                    description = "Description ",
                                    time = ZonedDateTime.now().plusHours(3).toEpochSecond().times(1000),
                                    isDone = false,
                                    remindAt = ZonedDateTime.now().toEpochSecond().times(1000)
                                ),
                            )
                        )
                        reminderDao.get().upsertReminders(
                            listOf(
                                ReminderEntity(
                                    id = "1",
                                    title = "Title REmin 1",
                                    description = "Description 1",
                                    time = ZonedDateTime.now().plusHours(1).plusMinutes(1).toEpochSecond().times(1000),
                                    remindAt = ZonedDateTime.now().toEpochSecond().times(1000)
                                ),
                            )
                        )
                    }

                }
            }
        ).build()
    }

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
}


