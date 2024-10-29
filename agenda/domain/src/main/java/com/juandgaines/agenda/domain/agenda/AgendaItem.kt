package com.juandgaines.agenda.domain.agenda

import java.time.ZonedDateTime

sealed interface AgendaItems{
    val id : String
    val title: String
    val description: String
    val date:ZonedDateTime

    data class Task(
        override val id:String,
        override val title:String,
        override val description:String,
        val time:ZonedDateTime,
        val remindAt:ZonedDateTime,
        val isDone:Boolean
    ):AgendaItems {
        override val date = time
    }

    data class Reminder(
        override val id:String,
        override val title:String,
        override val description:String,
        val time:ZonedDateTime,
        val remindAt:ZonedDateTime
    ):AgendaItems {
        override val date = time
    }
}


