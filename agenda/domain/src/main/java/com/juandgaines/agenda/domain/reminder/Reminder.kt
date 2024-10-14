package com.juandgaines.agenda.domain.reminder

import com.juandgaines.agenda.domain.agenda.AgendaItem

data class Reminder(
    val id:String,
    val title:String,
    val description:String?,
    val time:Long,
    val remindAt:Long
):AgendaItem {
    override val date = time
}