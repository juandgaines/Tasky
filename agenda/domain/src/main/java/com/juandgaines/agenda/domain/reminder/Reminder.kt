package com.juandgaines.agenda.domain.reminder

import com.juandgaines.agenda.domain.agenda.AgendaItem
import java.time.ZonedDateTime

data class Reminder(
    val id:String,
    val title:String,
    val description:String?,
    val time:ZonedDateTime,
    val remindAt:ZonedDateTime
):AgendaItem {
    override val date = time
}