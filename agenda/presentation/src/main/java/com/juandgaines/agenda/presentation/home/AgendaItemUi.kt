package com.juandgaines.agenda.presentation.home

import com.juandgaines.agenda.domain.agenda.AgendaItems
import java.time.ZonedDateTime

sealed interface AgendaItemUi{
    val date:ZonedDateTime

    data class Item(
        val agendaItem: AgendaItems,
        override val date : ZonedDateTime  = agendaItem.date
    ): AgendaItemUi

    data class Needle(
        override val date : ZonedDateTime = ZonedDateTime.now()
    ): AgendaItemUi
}