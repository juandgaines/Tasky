@file:OptIn(ExperimentalCoroutinesApi::class)

package com.juandgaines.agenda.presentation.agenda_item

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.juandgaines.agenda.domain.reminder.ReminderRepository
import com.juandgaines.agenda.domain.task.TaskRepository
import com.juandgaines.agenda.domain.utils.toUtcLocalDateTime
import com.juandgaines.agenda.domain.utils.toZonedDateTime
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemAction.Close
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemAction.Delete
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemAction.DismissDateDialog
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemAction.DismissTimeDialog
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemAction.Edit
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemAction.EditDescription
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemAction.EditTitle
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemAction.Save
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemAction.SelectAlarm
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemAction.SelectDateStart
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemAction.SelectTimeStart
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemAction.ShowDateDialog
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemAction.ShowTimeDialog
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
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.time.LocalTime
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class AgendaItemViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val taskRepository: TaskRepository,
    private val reminderRepository: ReminderRepository
):ViewModel() {
    private var eventChannel = Channel<AgendaItemEvent>()

    val events = eventChannel.receiveAsFlow()


    private val _state = MutableStateFlow(AgendaItemState())
    private var _isEditing:MutableStateFlow<Boolean> = MutableStateFlow(false)
    private var _navParameters=savedStateHandle.toRoute<AgendaItem>()

    val state:StateFlow<AgendaItemState> = _state
        .onStart {
            initState()
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

    private suspend fun initState() {
        _isEditing.value = _navParameters.isEditing
        val type = _navParameters.type
        val idItem = _navParameters.id
        val agendaType = AgendaItemOption.fromOrdinal(type)
        if (idItem != null) {
            when (agendaType) {
                REMINDER -> reminderRepository.getReminderById(idItem)
                TASK -> taskRepository.getTaskById(idItem)
                EVENT -> taskRepository.getTaskById(idItem)
            }.onSuccess { item ->
                _state.update {
                    _state.value.copy(
                        title = item.title,
                        description = item.description,
                        details = when (agendaType) {
                            REMINDER -> ReminderDetails
                            TASK -> TaskDetails
                            EVENT -> EventDetails()
                        },
                        startDateTime = item.date
                    )
                }
            }
        } else {
            _isEditing.value = true
            _state.update {
                _state.value.copy(
                    startDateTime = ZonedDateTime.now(),
                    details = when (AgendaItemOption.fromOrdinal(type)) {
                        REMINDER -> ReminderDetails
                        TASK -> TaskDetails
                        EVENT -> EventDetails()
                    }
                )
            }
        }
    }

    fun onAction(action: AgendaItemAction){
        when (action){
            is EditTitle -> {

            }
            is EditDescription -> {

            }
            is SelectDateStart -> {
                val zonedDate = action.dateMillis
                    .toUtcLocalDateTime()
                    .toZonedDateTime(
                        LocalTime.of(
                            _state.value.startDateTime.hour,
                            _state.value.startDateTime.minute
                        )
                    )
                _state.update {
                    state.value.copy(
                        startDateTime = zonedDate,
                        isSelectDateDialog = false
                    )
                }

            }
            is SelectTimeStart ->{
                _state.update {
                    state.value.copy(
                        startDateTime = state.value
                            .startDateTime
                            .withHour(action.hour)
                            .withMinute(action.minutes),
                        isSelectTimeDialog = false
                    )
                }

            }
            Delete -> {

            }
            Edit -> {
                _isEditing.value = true
            }
            Save -> {
                _isEditing.value = false
            }
            Close -> Unit
            DismissDateDialog -> {
                _state.update {
                    state.value.copy(
                        isSelectDateDialog = false
                    )
                }
            }
            DismissTimeDialog -> {
                _state.update {
                    state.value.copy(
                        isSelectTimeDialog = false
                    )
                }
            }
            ShowDateDialog -> {
                _state.update {
                    state.value.copy(
                        isSelectDateDialog = true
                    )
                }
            }
            ShowTimeDialog -> {
                _state.update {
                    state.value.copy(
                        isSelectTimeDialog = true
                    )
                }
            }

            is SelectAlarm -> {
                _state.update {
                    state.value.copy(
                        alarm = action.alarm
                    )
                }
            }
        }
    }
}