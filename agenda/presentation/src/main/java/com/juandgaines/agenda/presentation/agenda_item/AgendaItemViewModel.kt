@file:OptIn(ExperimentalCoroutinesApi::class)

package com.juandgaines.agenda.presentation.agenda_item

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.juandgaines.agenda.domain.agenda.AgendaItems.Reminder
import com.juandgaines.agenda.domain.agenda.AgendaItems.Task
import com.juandgaines.agenda.domain.reminder.ReminderRepository
import com.juandgaines.agenda.domain.task.TaskRepository
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemAction.Close
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemAction.Delete
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemAction.Edit
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemAction.EditDescription
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemAction.EditTitle
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemAction.Save
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemAction.SelectDateStart
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemAction.SelectTimeStart
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemDetails.EventDetails
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemDetails.ReminderDetails
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemDetails.TaskDetails
import com.juandgaines.agenda.presentation.home.AgendaItemOption
import com.juandgaines.agenda.presentation.home.AgendaItemOption.EVENT
import com.juandgaines.agenda.presentation.home.AgendaItemOption.REMINDER
import com.juandgaines.agenda.presentation.home.AgendaItemOption.TASK
import com.juandgaines.core.domain.util.onSuccess
import com.juandgaines.core.presentation.navigation.ScreenNav.AgendaItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class AgendaItemViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val taskRepository: TaskRepository,
    private val reminderRepository: ReminderRepository
):ViewModel() {
    private var _navParameters:MutableStateFlow<AgendaItem> = MutableStateFlow(savedStateHandle.toRoute<AgendaItem>())

    private var eventChannel = Channel<AgendaItemEvent>()
    val events = eventChannel.receiveAsFlow()
    private val _state = MutableStateFlow(AgendaItemState())

    private var _isEditing:MutableStateFlow<Boolean> = MutableStateFlow(_navParameters.value.isEditing)

    val state:StateFlow<AgendaItemState> = _navParameters
        .flatMapLatest { navParameters->
            loadAgendaItemFlow(navParameters)
        }
        .combine(
            _isEditing
        ){ state, isEditing->
            state.copy(
                isEditing = isEditing
            )
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            AgendaItemState()
        )

    private fun loadAgendaItemFlow(navParameters: AgendaItem) =
        flow<AgendaItemState> {
            val idItem = navParameters.id
            val agendaType=AgendaItemOption.fromOrdinal(navParameters.type)
            if (idItem != null) {
                when (agendaType) {
                    REMINDER -> reminderRepository.getReminderById(idItem)
                    TASK -> taskRepository.getTaskById(idItem)
                    EVENT -> taskRepository.getTaskById(idItem)
                }.onSuccess { item->
                    _state.update {
                        state.value.copy(
                            title = item.title,
                            description = item.description,
                            details = when (agendaType) {
                                REMINDER -> ReminderDetails
                                TASK-> TaskDetails
                                EVENT-> EventDetails()
                            },
                            startDateTime = item.date
                        )
                    }
                    emit(_state.value)
                }
            } else {
                _state.update {
                    state.value.copy(
                        startDateTime = ZonedDateTime.now(),
                        details = when (AgendaItemOption.fromOrdinal(navParameters.type)) {
                            REMINDER -> ReminderDetails
                            TASK -> TaskDetails
                            EVENT -> EventDetails()
                        }
                    )
                }
                emit(_state.value)
            }
        }

    fun onAction(action: AgendaItemAction){
        when (action){
            is EditTitle -> TODO()
            is EditDescription -> TODO()
            SelectDateStart -> TODO()
            SelectTimeStart -> TODO()
            Delete -> TODO()
            Edit -> {
                _isEditing.value = true
            }
            Save -> {
                _isEditing.value = false
            }
            Close -> Unit
        }
    }
}