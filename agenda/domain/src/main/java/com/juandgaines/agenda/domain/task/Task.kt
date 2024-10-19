package com.juandgaines.agenda.domain.task

import com.juandgaines.agenda.domain.agenda.AgendaItem
import com.juandgaines.agenda.domain.agenda.AgendaType
import java.time.ZonedDateTime

data class Task(
    override val id:String,
    val title:String,
    val description:String?,
    val time:ZonedDateTime,
    val remindAt:ZonedDateTime,
    val isDone:Boolean
):AgendaItem {
    override val type = AgendaType.Task
    override val date = time
}