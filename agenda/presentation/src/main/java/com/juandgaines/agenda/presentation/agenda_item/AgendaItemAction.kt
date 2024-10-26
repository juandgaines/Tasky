package com.juandgaines.agenda.presentation.agenda_item

sealed interface AgendaItemAction{
    data class EditTitle (val title: String): AgendaItemAction
    data class EditDescription (val description: String?): AgendaItemAction
    data object SelectDateStart: AgendaItemAction
    data object SelectTimeStart: AgendaItemAction
    data object Edit: AgendaItemAction
    data object Save: AgendaItemAction
    data object Close: AgendaItemAction
    data object Delete: AgendaItemAction
}