package com.juandgaines.agenda.domain.task

import com.juandgaines.agenda.domain.agenda.AgendaItem

data class Task(
    val id:String,
    val title:String,
    val description:String?,
    val time:Long,
    val remindAt:Long,
    val isDone:Boolean
):AgendaItem {
    override val date = time
}