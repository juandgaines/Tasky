package com.juandgaines.agenda.data.agenda.di

import android.content.Context
import com.juandgaines.agenda.data.agenda.AgendaPendingSyncScheduler
import com.juandgaines.agenda.data.agenda.DefaultAgendaRepository
import com.juandgaines.agenda.data.agenda.remote.AgendaApi
import com.juandgaines.agenda.domain.agenda.AgendaRepository
import com.juandgaines.agenda.domain.agenda.AgendaSyncScheduler
import com.juandgaines.agenda.domain.agenda.InitialsCalculator
import com.juandgaines.agenda.domain.reminder.ReminderRepository
import com.juandgaines.agenda.domain.task.TaskRepository
import com.juandgaines.core.data.database.agenda.AgendaSyncDao
import com.juandgaines.core.domain.auth.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
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

    @Provides
    @Singleton
    fun providesInitialsCalculator(
        sessionManager: SessionManager
    ): InitialsCalculator {
        return InitialsCalculator(sessionManager)
    }

    @Provides
    @Singleton
    fun providesAgendaRepository(
        reminderRepository: ReminderRepository,
        taskRepository: TaskRepository,
        agendaApi: AgendaApi,
        applicationScope: CoroutineScope
    ): AgendaRepository {
        return DefaultAgendaRepository(
            reminderRepository,
            taskRepository,
            agendaApi,
            applicationScope
        )
    }

    @Provides
    @Singleton
    fun provideAgendaSyncScheduler(
        @ApplicationContext context: Context,
        sessionManager: SessionManager,
        agendaSyncDao: AgendaSyncDao,
        applicationScope: CoroutineScope
    ): AgendaSyncScheduler {
        return AgendaPendingSyncScheduler(
            context,
            sessionManager,
            agendaSyncDao,
            applicationScope
        )
    }
}