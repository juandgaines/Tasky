package com.juandgaines.agenda.presentation.edit_field

sealed interface EditFieldAction {
    data object Save : EditFieldAction
}