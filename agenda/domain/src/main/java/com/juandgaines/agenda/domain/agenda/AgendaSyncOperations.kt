package com.juandgaines.agenda.domain.agenda

import kotlin.time.Duration

sealed interface AgendaSyncOperations {
    data class FetchAgendas(val interval: Duration): AgendaSyncOperations
    data class DeleteAgendaItem(val agendaItem: AgendaItems): AgendaSyncOperations
    data class UpdateAgendaItem(val agendaItem: AgendaItems): AgendaSyncOperations
    data class CreateAgendaItem(val agendaItem: AgendaItems): AgendaSyncOperations
}