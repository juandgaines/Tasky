package com.juandgaines.agenda.domain.agenda

interface AgendaSyncScheduler {
    suspend fun scheduleSync(syncType: AgendaSyncOperations)
    suspend fun cancelSync()
}