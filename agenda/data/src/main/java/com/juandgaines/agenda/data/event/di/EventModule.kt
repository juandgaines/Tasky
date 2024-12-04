package com.juandgaines.agenda.data.event.di

import android.content.Context
import com.juandgaines.agenda.data.agenda.LocalImageFileCompressor
import com.juandgaines.agenda.data.event.DefaultAttendeeRepository
import com.juandgaines.agenda.data.event.DefaultEventRepository
import com.juandgaines.agenda.data.event.remote.AttendeeApi
import com.juandgaines.agenda.data.event.remote.EventApi
import com.juandgaines.agenda.domain.agenda.AgendaSyncScheduler
import com.juandgaines.agenda.domain.agenda.FileCompressor
import com.juandgaines.agenda.domain.event.AttendeeRepository
import com.juandgaines.agenda.domain.event.EventRepository
import com.juandgaines.core.data.database.event.EventDao
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
class EventModule {
    @Provides
    @Singleton
    fun provideEventApi(retrofit: Retrofit): EventApi {
        return retrofit.create(EventApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAttendeeApi(retrofit: Retrofit): AttendeeApi {
        return retrofit.create(AttendeeApi::class.java)
    }

    @Provides
    @Singleton
    fun provideEventRepository(
        eventDao: EventDao,
        eventApi: EventApi,
        applicationScope: CoroutineScope,
        agendaSyncScheduler: AgendaSyncScheduler,
        sessionManager: SessionManager
    ): EventRepository {
        return DefaultEventRepository(eventDao, eventApi,applicationScope,agendaSyncScheduler, sessionManager)
    }

    @Provides
    @Singleton
    fun provideAttendee(
        attendeeApi: AttendeeApi,
    ):AttendeeRepository {
        return DefaultAttendeeRepository(
            attendeeApi,
        )
    }

    @Provides
    @Singleton
    fun providesFileCompressor(
        @ApplicationContext
        context: Context
    ): FileCompressor {
        return LocalImageFileCompressor(context)
    }
}