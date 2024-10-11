package com.juandgaines.agenda.data.reminder.di

import com.juandgaines.agenda.data.reminder.local.RoomLocalReminderDataSource
import com.juandgaines.agenda.data.reminder.remote.ReminderApi
import com.juandgaines.agenda.data.reminder.remote.RetrofitRemoteReminderDataSource
import com.juandgaines.agenda.domain.reminder.LocalReminderDataSource
import com.juandgaines.agenda.domain.reminder.RemoteReminderDataSource
import com.juandgaines.core.data.database.reminder.ReminderDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ReminderModule {
    @Provides
    @Singleton
    fun provideReminderApi(retrofit: Retrofit): ReminderApi {
        return retrofit.create(ReminderApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRoomLocalReminderDataSource(
        reminderDao: ReminderDao
    ): LocalReminderDataSource {
        return RoomLocalReminderDataSource(reminderDao)
    }

    @Provides
    @Singleton
    fun provideRetrofitRemoteReminderDataSource(
        reminderApi: ReminderApi
    ): RemoteReminderDataSource {
        return RetrofitRemoteReminderDataSource(reminderApi)
    }
}