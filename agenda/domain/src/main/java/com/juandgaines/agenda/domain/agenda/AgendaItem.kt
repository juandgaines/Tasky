package com.juandgaines.agenda.domain.agenda

import com.juandgaines.core.domain.agenda.AgendaItemOption
import java.time.ZonedDateTime

sealed interface AgendaItems{
    val id : String
    val title: String
    val description: String
    val date:ZonedDateTime
    val dateEnd:ZonedDateTime?
    val alarmDate:ZonedDateTime
    val agendaItemOption: AgendaItemOption
    val agendaItemDetails:AgendaItemDetails

    data class Task(
        override val id:String,
        override val title:String,
        override val description:String,
        val time:ZonedDateTime,
        val remindAt:ZonedDateTime,
        val isDone:Boolean
    ):AgendaItems {
        override val date = time
        override val dateEnd = null
        override val alarmDate = remindAt
        override val agendaItemOption = AgendaItemOption.TASK
        override val agendaItemDetails = AgendaItemDetails.TaskDetails(isDone)
    }

    data class Reminder(
        override val id:String,
        override val title:String,
        override val description:String,
        val time:ZonedDateTime,
        val remindAt:ZonedDateTime
    ):AgendaItems {
        override val date = time
        override val dateEnd = null
        override val alarmDate = remindAt
        override val agendaItemOption = AgendaItemOption.REMINDER
        override val agendaItemDetails = AgendaItemDetails.ReminderDetails
    }

    data class Event(
        override val id:String,
        override val title:String,
        override val description:String,
        val time:ZonedDateTime,
        val endTime:ZonedDateTime,
        val remindAt:ZonedDateTime,
        val host:String,
        val isUserEventCreator:Boolean,
        val attendee: List<Attendee>
    ):AgendaItems{
        override val date = time
        override val dateEnd = endTime
        override val alarmDate = remindAt
        override val agendaItemOption = AgendaItemOption.EVENT
        override val agendaItemDetails = AgendaItemDetails.EventDetails(
            finishDate =  endTime,
            host = host,
            isUserCreator =  isUserEventCreator,
            attendees = attendee
        )
    }

    companion object{
        const val EDIT_FIELD_TITLE_KEY = "title"
        const val EDIT_FIELD_TITLE_DESCRIPTION = "description"
    }
}


