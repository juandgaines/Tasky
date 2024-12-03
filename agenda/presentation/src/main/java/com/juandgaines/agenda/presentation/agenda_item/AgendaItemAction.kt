package com.juandgaines.agenda.presentation.agenda_item

sealed interface AgendaItemAction{
    data class EditField(val key:String, val value :String): AgendaItemAction
    data class UpdateField(val key: String, val value: String): AgendaItemAction
    data class ShowTimeDialog(val isEndDate:Boolean =false): AgendaItemAction
    data class ShowDateDialog(val isEndDate: Boolean = false): AgendaItemAction
    data object DismissTimeDialog: AgendaItemAction
    data object DismissDateDialog: AgendaItemAction
    data class SelectDateStart(val dateMillis:Long): AgendaItemAction
    data class SelectTimeStart(val hour:Int, val minutes:Int): AgendaItemAction
    data class SelectDateFinish(val dateMillis:Long): AgendaItemAction
    data class SelectTimeFinish(val hour:Int, val minutes:Int): AgendaItemAction
    data class SelectAlarm(val alarm: AlarmOptions): AgendaItemAction
    data class SelectAttendeeFilter(val filter: AttendeeFilter) : AgendaItemAction
    data object ShowAttendeeDialog : AgendaItemAction
    data object DismissAttendeeDialog : AgendaItemAction
    data class AddEmailAsAttendee(val email: String) : AgendaItemAction
    data class RemoveAttendee(val attendeeId: String) : AgendaItemAction
    data object Edit: AgendaItemAction
    data object Save: AgendaItemAction
    data object Close: AgendaItemAction
    data object Delete: AgendaItemAction
    data object Leave: AgendaItemAction
    data object Join: AgendaItemAction
}