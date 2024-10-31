package com.juandgaines.agenda.presentation.edit_field

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.juandgaines.agenda.presentation.edit_field.EditFieldAction.Save
import com.juandgaines.core.presentation.navigation.ScreenNav
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class EditFieldViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private var eventChannel = Channel<EditFieldEvent>()
    val events = eventChannel.receiveAsFlow()

    private var _state  = MutableStateFlow(EditFieldState())
    private val editValueData =  savedStateHandle.toRoute<ScreenNav.EditField>()

    var state = _state
        .onStart {
            _state.value = EditFieldState(
                fieldName = editValueData.fieldName,
                fieldValue = TextFieldState(editValueData.fieldValue))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = EditFieldState()
        )

    fun onAction(action: EditFieldAction) {
        when (action) {
            Save -> Unit
            EditFieldAction.Close -> Unit
        }
    }
}