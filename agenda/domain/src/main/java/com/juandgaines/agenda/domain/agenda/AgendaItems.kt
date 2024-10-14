package com.juandgaines.agenda.domain.agenda

sealed interface AgendaItems{
    data class Task(
        val id:String,
        val title:String,
        val description:String?,
        val time:Long,
        val remindAt:Long,
        val isDone:Boolean
    ):AgendaItems

    data class Reminder(
        val id:String,
        val title:String,
        val description:String?,
        val time:Long,
        val remindAt:Long
    ):AgendaItems
}