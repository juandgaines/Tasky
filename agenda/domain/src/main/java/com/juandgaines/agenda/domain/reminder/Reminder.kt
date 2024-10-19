package com.juandgaines.agenda.domain.reminder

import com.juandgaines.agenda.domain.agenda.AgendaItem
import com.juandgaines.agenda.domain.agenda.AgendaType
import java.time.ZonedDateTime

data class Reminder(
    override val id:String,
    val title:String,
    val description:String?,
    val time:ZonedDateTime,
    val remindAt:ZonedDateTime
):AgendaItem {
    override val type = AgendaType.Reminder
    override val date = time
}