package com.juandgaines.agenda.domain.task

data class Task(
    val id:String,
    val title:String,
    val description:String?,
    val time:Long,
    val remindAt:Long,
    val isDone:Boolean
)