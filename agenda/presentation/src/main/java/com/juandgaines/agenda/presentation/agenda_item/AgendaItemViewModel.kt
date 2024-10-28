@file:OptIn(ExperimentalCoroutinesApi::class)

package com.juandgaines.agenda.presentation.agenda_item

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.juandgaines.agenda.domain.agenda.AgendaItems
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
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
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

    var state by mutableStateOf(AgendaItemState())
        private set

    private var eventChannel = Channel<AgendaItemEvent>()
    val events = eventChannel.receiveAsFlow()


    //private val navParameters = savedStateHandle.getStateFlow<ScreenNav.AgendaItem>("")
    private var _navParameters:MutableStateFlow<AgendaItem> = MutableStateFlow(savedStateHandle.toRoute<AgendaItem>())
    val taskInfo:StateFlow<AgendaItems?> = _navParameters
        .flatMapLatest { navParameters->
            flow<AgendaItems?>{
                val idItem = navParameters.id
                state = state.copy(
                    isEditing = navParameters.isEditing
                )
                if(idItem != null) {
                    when (AgendaItemOption.fromOrdinal(navParameters.type)) {
                        REMINDER -> reminderRepository.getReminderById(idItem)
                        TASK -> taskRepository.getTaskById(idItem)
                        EVENT -> null
                    }?.onSuccess {
                        emit(it)
                    }
                }
            }
        }.onEach { agendaItem ->
            when(agendaItem){
                is Reminder -> {
                    state = state.copy(
                        title = agendaItem.title,
                        description = agendaItem.description,
                        details = ReminderDetails
                    )
                }
                is Task -> {
                    state = state.copy(
                        title = agendaItem.title,
                        description = agendaItem.description,
                        details = TaskDetails
                    )
                }
                null -> {
                    state = state.copy(
                        title = "",
                        description = "",
                        details = when(AgendaItemOption.fromOrdinal( _navParameters.value.type)){
                            REMINDER -> ReminderDetails
                            TASK -> TaskDetails
                            EVENT -> EventDetails()
                        },
                        startDateTime = ZonedDateTime.now()
                    )
                }
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            null
        )

    fun onAction(action: AgendaItemAction){
        when (action){
            is EditTitle -> TODO()
            is EditDescription -> TODO()
            SelectDateStart -> TODO()
            SelectTimeStart -> TODO()
            Delete -> TODO()
            Edit -> {
                state = state.copy(
                    isEditing = true
                )
            }
            Save -> TODO()
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