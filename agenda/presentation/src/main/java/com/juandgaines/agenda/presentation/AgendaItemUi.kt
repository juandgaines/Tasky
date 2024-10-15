package com.juandgaines.agenda.presentation

import com.juandgaines.agenda.domain.agenda.AgendaItem
import java.time.ZonedDateTime

sealed interface AgendaItemUi{
    val date:ZonedDateTime

    data class Item(
        val agendaItem: AgendaItem,
        override val date : ZonedDateTime  = agendaItem.date
    ):AgendaItemUi

    data class Needle(
        override val date : ZonedDateTime = ZonedDateTime.now()
    ):AgendaItemUi
}