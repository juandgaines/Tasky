package com.juandgaines.agenda.domain.reminder

data class Reminder(
    val id:String,
    val title:String,
    val description:String?,
    val time:Long,
    val remindAt:Long
)