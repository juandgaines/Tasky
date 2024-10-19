package com.juandgaines.agenda.domain.agenda

import kotlin.time.Duration

sealed interface AgendaSyncOperations {
    data class FetchAgendas(val interval: Duration): AgendaSyncOperations
    data class DeleteAgendaItem(val agendaId: AgendaItem): AgendaSyncOperations
    data class UpdateAgendaItem(val agendaItem: AgendaItem): AgendaSyncOperations
    data class CreateAgendaItem(val agendaItem: AgendaItem): AgendaSyncOperations
}