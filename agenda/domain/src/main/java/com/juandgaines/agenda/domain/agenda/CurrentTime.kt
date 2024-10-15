package com.juandgaines.agenda.domain.agenda

import java.time.ZonedDateTime

data class CurrentTime(
    val time: ZonedDateTime
):AgendaItem {
    override val date = time
}