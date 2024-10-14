package com.juandgaines.agenda.presentation

sealed interface AgendaEvents {
    data object LogOut : AgendaEvents
}