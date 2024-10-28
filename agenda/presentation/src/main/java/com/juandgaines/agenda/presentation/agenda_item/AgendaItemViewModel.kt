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

    //private val navParameters = savedStateHandle.getStateFlow<ScreenNav.AgendaItem>("")

    private var _isEditing:MutableStateFlow<Boolean> = MutableStateFlow(_navParameters.value.isEditing)

    val state:StateFlow<AgendaItemState> = _navParameters
        .flatMapLatest { navParameters->
            flow<AgendaItemState>{
                val idItem = navParameters.id
                if(idItem != null) {
                    when (AgendaItemOption.fromOrdinal(navParameters.type)) {
                        REMINDER -> reminderRepository.getReminderById(idItem)
                        TASK -> taskRepository.getTaskById(idItem)
                        EVENT -> null
                    }?.onSuccess {
                        _state.value = _state.value.copy(
                            title = it.title,
                            description = it.description,
                            details = when(it){
                                is Reminder -> ReminderDetails
                                is Task -> TaskDetails
                            },
                            startDateTime = it.date
                        )
                        emit(_state.value)
                    }
                }
                else {
                     _state.value = _state.value.copy(
                        startDateTime = ZonedDateTime.now(),
                        details = when (AgendaItemOption.fromOrdinal(navParameters.type)) {
                            REMINDER -> ReminderDetails
                            TASK -> TaskDetails
                            EVENT -> EventDetails()
                        }
                    )
                    emit(_state.value)
                }
            }
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

/*
            val idItem = navParameters.id
            state = state.copy(
                isEditing = navParameters.isEditing
            )
            val flow = if (idItem != null) {
                when (AgendaItemOption.fromOrdinal( navParameters.type)) {
                    REMINDER -> reminderRepository.getReminderByIdFlow(idItem)
                    TASK ->  taskRepository.getTaskByIdFlow(idItem)
                    EVENT -> flowOf()
                }
            } else {
                flowOf(null)
            }
            flow
            */