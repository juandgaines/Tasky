package com.juandgaines.agenda.presentation.edit_field

import androidx.compose.foundation.text.input.TextFieldState

data class EditFieldState(
    val fieldName:String = "",
    val fieldValue:TextFieldState = TextFieldState(),
)