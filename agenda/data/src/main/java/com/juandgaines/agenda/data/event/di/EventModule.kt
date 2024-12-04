package com.juandgaines.agenda.data.event.di

import com.juandgaines.agenda.data.event.DefaultAttendeeRepository
import com.juandgaines.agenda.data.event.DefaultEventRepository
import com.juandgaines.agenda.data.event.remote.AttendeeApi
import com.juandgaines.agenda.data.event.remote.EventApi
import com.juandgaines.agenda.domain.agenda.AgendaSyncScheduler
import com.juandgaines.agenda.domain.event.AttendeeRepository
import com.juandgaines.agenda.domain.event.EventRepository
import com.juandgaines.core.data.database.event.EventDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
        agendaSyncScheduler: AgendaSyncScheduler
    ): EventRepository {
        return DefaultEventRepository(eventDao, eventApi,applicationScope,agendaSyncScheduler)
    }

    @Provides
    @Singleton
    fun provideAttendee(
        attendeeApi: AttendeeApi,
        eventDao: EventDao,
        agendaItemScheduler: AgendaSyncScheduler
    ):AttendeeRepository {
        return DefaultAttendeeRepository(
            attendeeApi,
            eventDao,
            agendaItemScheduler
        )
    }
}