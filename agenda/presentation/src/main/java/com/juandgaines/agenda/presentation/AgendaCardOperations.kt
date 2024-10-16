package com.juandgaines.agenda.presentation

import com.juandgaines.agenda.domain.agenda.AgendaItem

sealed interface AgendaCardOperations{
    data class Open(val agendaItem: AgendaItem): AgendaCardOperations
    data class Edit(val agendaItem: AgendaItem): AgendaCardOperations
    data class Delete(val agendaItem: AgendaItem): AgendaCardOperations
}