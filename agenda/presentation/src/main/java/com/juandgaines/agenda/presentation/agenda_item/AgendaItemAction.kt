package com.juandgaines.agenda.presentation.agenda_item

sealed interface AgendaItemAction{
    data class EditTitle (val title: String): AgendaItemAction
    data class EditDescription (val description: String?): AgendaItemAction
    data object ShowTimeDialog: AgendaItemAction
    data object ShowDateDialog: AgendaItemAction
    data object DismissTimeDialog: AgendaItemAction
    data object DismissDateDialog: AgendaItemAction
    data class SelectDateStart(val dateMillis:Long): AgendaItemAction
    data class SelectTimeStart(val hour:Int, val minutes:Int): AgendaItemAction
    data object Edit: AgendaItemAction
    data object Save: AgendaItemAction
    data object Close: AgendaItemAction
    data object Delete: AgendaItemAction
}