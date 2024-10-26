package com.juandgaines.agenda.presentation.agenda_item

import com.juandgaines.agenda.domain.agenda.AgendaItem
import com.juandgaines.agenda.domain.agenda.AgendaType

data class AgendaItemState(
    val isEditing: Boolean = true,
    val title: String = "",
    val description: String? = null,
    val agendaItem: AgendaItem,
    val agendaType: AgendaType
)