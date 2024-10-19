package com.juandgaines.agenda.domain.agenda

interface AgendaSyncScheduler {
    fun scheduleSync(syncType: AgendaSyncOperations)
    fun cancelSync()
}