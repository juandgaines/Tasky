package com.juandgaines.agenda.presentation.edit_field

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.juandgaines.agenda.presentation.edit_field.EditFieldAction.Save
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class EditFieldViewModel @Inject constructor(): ViewModel() {

    private var eventChannel = Channel<EditFieldEvent>()
    val events = eventChannel.receiveAsFlow()

    var state by mutableStateOf(EditFieldState())
        private set

    fun onAction(action: EditFieldAction) {
        when (action) {
            Save -> TODO()
        }
    }
}