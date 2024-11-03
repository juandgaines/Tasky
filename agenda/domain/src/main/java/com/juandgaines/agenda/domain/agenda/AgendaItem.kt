package com.juandgaines.agenda.domain.agenda

import com.juandgaines.core.domain.agenda.AgendaItemOption
import java.time.ZonedDateTime

sealed interface AgendaItems{
    val id : String
    val title: String
    val description: String
    val date:ZonedDateTime
    val agendaItemOption: AgendaItemOption

    data class Task(
        override val id:String,
        override val title:String,
        override val description:String,
        val time:ZonedDateTime,
        val remindAt:ZonedDateTime,
        val isDone:Boolean
    ):AgendaItems {
        override val date = time
        override val agendaItemOption = AgendaItemOption.TASK
    }

    data class Reminder(
        override val id:String,
        override val title:String,
        override val description:String,
        val time:ZonedDateTime,
        val remindAt:ZonedDateTime
    ):AgendaItems {
        override val date = time
        override val agendaItemOption = AgendaItemOption.REMINDER
    }

    data object Event:AgendaItems{
        override val id = ""
        override val title = ""
        override val description = ""
        override val date = ZonedDateTime.now()
        override val agendaItemOption = AgendaItemOption.EVENT
    }

    companion object{
        const val EDIT_FIELD_TITLE_KEY = "title"
        const val EDIT_FIELD_TITLE_DESCRIPTION = "description"
    }
}


