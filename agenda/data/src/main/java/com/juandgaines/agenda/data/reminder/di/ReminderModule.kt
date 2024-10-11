package com.juandgaines.agenda.data.reminder.di

import com.juandgaines.agenda.data.reminder.DefaultReminderRepository
import com.juandgaines.agenda.data.reminder.remote.ReminderApi
import com.juandgaines.agenda.domain.reminder.ReminderRepository
import com.juandgaines.core.data.database.reminder.ReminderDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
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
    fun provideReminderRepository(
        reminderDao: ReminderDao,
        reminderApi: ReminderApi,
        applicationScope: CoroutineScope
    ): ReminderRepository {
        return DefaultReminderRepository(reminderDao, reminderApi, applicationScope)
    }
}