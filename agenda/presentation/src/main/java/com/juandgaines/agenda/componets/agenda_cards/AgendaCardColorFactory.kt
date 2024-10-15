package com.juandgaines.agenda.componets.agenda_cards

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.juandgaines.agenda.domain.agenda.AgendaItem
import com.juandgaines.agenda.domain.reminder.Reminder
import com.juandgaines.agenda.domain.task.Task

object AgendaCardColorFactory {

    @Composable
    fun getColors(agendaItem: AgendaItem): AgendaCardColors {
        return when (agendaItem) {
            is Task -> {
                AgendaCardColors(
                    colorBackground = MaterialTheme.colorScheme.primary,
                    colorPrimaryText = MaterialTheme.colorScheme.onSurface,
                    colorSecondaryText = MaterialTheme.colorScheme.onSurface,
                    colorCheck = MaterialTheme.colorScheme.onSurface
                )
            }
            is Reminder->{
                AgendaCardColors(
                    colorBackground = MaterialTheme.colorScheme.primaryContainer,
                    colorPrimaryText = MaterialTheme.colorScheme.surface,
                    colorSecondaryText = MaterialTheme.colorScheme.onPrimary,
                    colorCheck = MaterialTheme.colorScheme.surface
                )
            }
            else -> {
                AgendaCardColors(
                    colorBackground = MaterialTheme.colorScheme.primaryContainer,
                    colorPrimaryText = MaterialTheme.colorScheme.surface,
                    colorSecondaryText = MaterialTheme.colorScheme.onPrimary,
                    colorCheck = MaterialTheme.colorScheme.surface
                )
            }
        }
    }
}