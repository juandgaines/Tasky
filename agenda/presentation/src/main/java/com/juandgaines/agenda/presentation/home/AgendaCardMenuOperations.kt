package com.juandgaines.agenda.presentation.home

import com.juandgaines.agenda.domain.agenda.AgendaItem

sealed interface AgendaCardMenuOperations{
    data class Open(val agendaItem: AgendaItem): AgendaCardMenuOperations
    data class Edit(val agendaItem: AgendaItem): AgendaCardMenuOperations
    data class Delete(val agendaItem: AgendaItem): AgendaCardMenuOperations
}