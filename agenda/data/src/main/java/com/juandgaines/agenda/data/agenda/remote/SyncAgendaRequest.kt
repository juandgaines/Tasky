package com.juandgaines.agenda.data.agenda.remote

import kotlinx.serialization.Serializable

@Serializable
data class SyncAgendaRequest(
    val deletedEventIds: List<String>,
    val deletedTaskIds: List<String>,
    val deletedReminderIds: List<String>,
)