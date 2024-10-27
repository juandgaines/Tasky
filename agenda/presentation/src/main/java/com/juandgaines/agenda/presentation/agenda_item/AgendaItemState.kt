package com.juandgaines.agenda.presentation.agenda_item


import com.juandgaines.agenda.domain.agenda.AgendaItems
import com.juandgaines.agenda.domain.agenda.AgendaType

data class AgendaItemState(
    val isEditing: Boolean = true,
    val title: String = "",
    val description: String? = null,
    val agendaItem: AgendaItems,
    val agendaType: AgendaType
)