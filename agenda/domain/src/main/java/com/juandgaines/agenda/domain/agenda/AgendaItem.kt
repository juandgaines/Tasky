package com.juandgaines.agenda.domain.agenda

import java.time.ZonedDateTime

interface AgendaItem{
    val title:String
    val description:String?
    val date:ZonedDateTime
}



