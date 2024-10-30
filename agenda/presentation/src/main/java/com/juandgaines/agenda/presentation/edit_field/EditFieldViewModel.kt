package com.juandgaines.agenda.presentation.edit_field

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.juandgaines.agenda.presentation.edit_field.EditFieldAction.Save
import com.juandgaines.core.presentation.navigation.ScreenNav
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class EditFieldViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private var eventChannel = Channel<EditFieldEvent>()
    val events = eventChannel.receiveAsFlow()

    var state by mutableStateOf(EditFieldState())
        private set

    private val editValueData =  savedStateHandle.toRoute<ScreenNav.EditField>()

    init {
        state = state.copy(
            fieldName = editValueData.fieldName,
            fieldValue = TextFieldState(editValueData.fieldValue)
        )
    }

    fun onAction(action: EditFieldAction) {
        when (action) {
            Save -> Unit
            EditFieldAction.Close -> Unit
        }
    }
}