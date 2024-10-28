package com.juandgaines.agenda.presentation.agenda_item

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.juandgaines.agenda.domain.reminder.ReminderRepository
import com.juandgaines.agenda.domain.task.TaskRepository
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemAction.Delete
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemAction.Edit
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemAction.EditDescription
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemAction.EditTitle
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemAction.Save
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemAction.SelectDateStart
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemAction.SelectTimeStart
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemDetails.ReminderDetails
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemDetails.TaskDetails
import com.juandgaines.agenda.presentation.home.AgendaItemOption
import com.juandgaines.core.domain.util.onSuccess
import com.juandgaines.core.presentation.navigation.ScreenNav
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
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


    private val navParameters = savedStateHandle.toRoute<ScreenNav.AgendaItem>()

    init {
        viewModelScope.launch {
            val idItem = navParameters.id
            state = state.copy(
                isEditing = navParameters.isEditing
            )
            val type = AgendaItemOption.fromOrdinal(navParameters.type)
            when (type){
                AgendaItemOption.REMINDER ->{
                    if (idItem != null){
                        val response = reminderRepository.getReminderById(idItem)
                        response.onSuccess { reminder->
                            state = state.copy(
                                title = reminder.title,
                                description = reminder.description,
                                startDateTime = reminder.time,
                                details = ReminderDetails
                            )
                        }
                    }
                    else {
                        state = state.copy(
                            details = ReminderDetails
                        )
                    }
                }
                AgendaItemOption.TASK -> {
                    if (idItem != null){
                        val response = taskRepository.getTaskById(idItem)
                        response.onSuccess { task->
                            state = state.copy(
                                title = task.title,
                                description = task.description,
                                startDateTime = task.time,
                                details = TaskDetails
                            )
                        }
                    }
                    else{
                        state = state.copy(
                            details = TaskDetails
                        )
                    }

                }
                AgendaItemOption.EVENT ->{

                }
            }
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
                state = state.copy(
                    isEditing = true
                )
            }
            Save -> TODO()
            else -> Unit
        }
    }
}