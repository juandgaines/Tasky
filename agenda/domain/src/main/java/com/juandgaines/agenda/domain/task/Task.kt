package com.juandgaines.agenda.domain.task

import com.juandgaines.agenda.domain.agenda.AgendaItem
import java.time.ZonedDateTime

data class Task(
    val id:String,
    override val title:String,
    override val description:String?,
    val time:ZonedDateTime,
    val remindAt:ZonedDateTime,
    val isDone:Boolean
):AgendaItem {
    override val date = time
}