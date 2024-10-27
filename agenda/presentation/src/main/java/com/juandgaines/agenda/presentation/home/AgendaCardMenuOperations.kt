package com.juandgaines.agenda.presentation.home

import com.juandgaines.agenda.domain.agenda.AgendaItems

sealed interface AgendaCardMenuOperations{
    data class Open(val agendaItem: AgendaItems): AgendaCardMenuOperations
    data class Edit(val agendaItem: AgendaItems): AgendaCardMenuOperations
    data class Delete(val agendaItem: AgendaItems): AgendaCardMenuOperations
}