@file:OptIn(ExperimentalCoroutinesApi::class)

package com.juandgaines.agenda.presentation.agenda_item

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.juandgaines.agenda.domain.agenda.AgendaItems
import com.juandgaines.agenda.domain.agenda.AgendaItems.Event
import com.juandgaines.agenda.domain.agenda.AgendaItems.Reminder
import com.juandgaines.agenda.domain.agenda.AgendaItems.Task
import com.juandgaines.agenda.domain.agenda.AlarmScheduler
import com.juandgaines.agenda.domain.reminder.ReminderRepository
import com.juandgaines.agenda.domain.task.TaskRepository
import com.juandgaines.agenda.domain.utils.isToday
import com.juandgaines.agenda.domain.utils.toUtcLocalDateTime
import com.juandgaines.agenda.domain.utils.toZonedDateTime
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemAction.Close
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemAction.Delete
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemAction.DismissDateDialog
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemAction.DismissTimeDialog
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemAction.Edit
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemAction.EditField
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemAction.Save
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemAction.SelectAlarm
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemAction.SelectDateStart
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemAction.SelectTimeStart
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemAction.ShowDateDialog
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemAction.ShowTimeDialog
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemAction.UpdateField
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemDetails.EventDetails
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemDetails.ReminderDetails
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemDetails.TaskDetails
import com.juandgaines.agenda.presentation.agenda_item.AlarmOptions.DAY_ONE
import com.juandgaines.agenda.presentation.agenda_item.AlarmOptions.HOUR_ONE
import com.juandgaines.agenda.presentation.agenda_item.AlarmOptions.HOUR_SIX
import com.juandgaines.agenda.presentation.agenda_item.AlarmOptions.MINUTES_TEN
import com.juandgaines.agenda.presentation.agenda_item.AlarmOptions.MINUTES_THIRTY
import com.juandgaines.core.domain.agenda.AgendaItemOption
import com.juandgaines.core.domain.agenda.AgendaItemOption.EVENT
import com.juandgaines.core.domain.agenda.AgendaItemOption.REMINDER
import com.juandgaines.core.domain.agenda.AgendaItemOption.TASK
import com.juandgaines.core.domain.util.EmptyDataResult
import com.juandgaines.core.domain.util.Result
import com.juandgaines.core.domain.util.onError
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
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.ZonedDateTime
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AgendaItemViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val taskRepository: TaskRepository,
    private val reminderRepository: ReminderRepository,
    private val alarmScheduler: AlarmScheduler
):ViewModel() {

    private var eventChannel = Channel<AgendaItemEvent>()
    val events = eventChannel.receiveAsFlow()

    private val _state = MutableStateFlow(AgendaItemState())
    private var _isEditing:MutableStateFlow<Boolean> = MutableStateFlow(false)
    private var _navParameters=savedStateHandle.toRoute<AgendaItem>()
    private var _isInit: Boolean = false
    private var _agendaItemBuffer: AgendaItems? = null
    private val _type = AgendaItemOption.fromOrdinal( _navParameters.type)

    val state:StateFlow<AgendaItemState> = _state
        .onStart {
            if (!_isInit){
                initState()
                _isInit = true
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

    private suspend fun initState() {
        _isEditing.value = _navParameters.isEditing

        val idItem = _navParameters.id
        if (idItem != null) {
            when (_type) {
                REMINDER -> reminderRepository.getReminderById(idItem)
                TASK -> taskRepository.getTaskById(idItem)
                EVENT -> taskRepository.getTaskById(idItem)
            }.onSuccess { item ->
                _agendaItemBuffer = item
                updateState {
                    it.copy(
                        isNew = false,
                        title = item.title,
                        description = item.description,
                        details = when (_type) {
                            REMINDER -> ReminderDetails
                            TASK -> TaskDetails(
                                isCompleted = (item as Task).isDone
                            )
                            EVENT -> EventDetails()
                        },
                        startDateTime = item.date
                    )
                }
            }
        } else {
            _isEditing.value = true
            updateState {
                val isToday = _navParameters.dateEpochMillis?.toZonedDateTime()?.isToday()
                val initialDate = if (isToday == true) {
                    ZonedDateTime.now()
                } else {
                    _navParameters.dateEpochMillis
                        ?.toZonedDateTime()
                        ?.withHour(0)
                        ?.withMinute(0)
                }
                it.copy(
                    isNew = true,
                    startDateTime =initialDate ?: ZonedDateTime.now(),
                    details = when (_type) {
                        REMINDER -> ReminderDetails
                        TASK -> TaskDetails()
                        EVENT -> EventDetails()
                    }
                )
            }
        }
    }

    fun onAction(action: AgendaItemAction){
        viewModelScope.launch {
        when (action){
            is EditField ->  Unit
            is SelectDateStart -> {
                val zonedDate = action.dateMillis
                    .toUtcLocalDateTime()
                    .toZonedDateTime(
                        LocalTime.of(
                            _state.value.startDateTime.hour,
                            _state.value.startDateTime.minute
                        )
                    )
                updateState {
                    it.copy(
                        startDateTime = zonedDate,
                        isSelectDateDialog = false
                    )
                }

                }
                is SelectTimeStart ->{
                    updateState {
                        it.copy(
                            startDateTime = it.startDateTime
                                .withHour(action.hour)
                                .withMinute(action.minutes),
                            isSelectTimeDialogVisible = false
                        )
                    }

                }
                Delete -> {
                    val agendaItemId = _navParameters.id
                    if (agendaItemId != null) {
                        val result = when (_type) {
                            REMINDER -> reminderRepository.deleteReminder(
                                Reminder(
                                    id = agendaItemId,
                                    title = _state.value.title,
                                    description = _state.value.description,
                                    time = _state.value.startDateTime,
                                    remindAt = _state.value.startDateTime
                                )
                            )
                            TASK -> taskRepository.deleteTask(
                                Task(
                                    id = agendaItemId,
                                    title = _state.value.title,
                                    description = _state.value.description,
                                    time = _state.value.startDateTime,
                                    isDone = (_state.value.details as TaskDetails).isCompleted,
                                    remindAt = _state.value.startDateTime
                                )
                            )
                            EVENT -> {
                                Result.Success(Unit)
                            }
                        }
                        result
                            .onSuccess {
                                _agendaItemBuffer?.run {
                                    alarmScheduler.cancelAlarm(this)
                                }
                                eventChannel.send(AgendaItemEvent.Deleted)
                            }.onError {
                                eventChannel.send(AgendaItemEvent.DeletionScheduled)
                            }
                    }

                }
            Edit -> {
                _isEditing.value = true
                }
                Save -> {
                    val agendaItemId = _navParameters.id
                    val desiredAlarmDate = calculateTimeAlarm()


                    if (agendaItemId != null) {
                        val data = when (_type) {
                            REMINDER -> {
                                Reminder(
                                    id = agendaItemId,
                                    title = _state.value.title,
                                    description = _state.value.description,
                                    time = _state.value.startDateTime,
                                    remindAt = desiredAlarmDate
                                )
                            }
                            TASK -> {
                                Task(
                                    id = agendaItemId,
                                    title = _state.value.title,
                                    description = _state.value.description,
                                    time = _state.value.startDateTime,
                                    isDone = (_state.value.details as TaskDetails).isCompleted,
                                    remindAt = desiredAlarmDate
                                )
                            }
                            EVENT -> {
                                Event
                            }
                        }

                        val response = when (data) {
                            is Reminder -> reminderRepository.updateReminder(
                                data
                            )
                            is Task -> taskRepository.updateTask(
                                data
                            )
                            Event -> {
                                //TODO: Implement update event
                                Result.Success(Unit)
                            }
                        }
                        response
                            .onSuccess {
                                _agendaItemBuffer?.run {
                                    alarmScheduler.cancelAlarm(this)
                                }
                                alarmScheduler.scheduleAlarm(data)
                                eventChannel.send(AgendaItemEvent.Updated)
                            }.onError {
                                eventChannel.send(AgendaItemEvent.UpdateScheduled)
                            }
                    } else {
                        val data = when (_type) {
                            REMINDER -> {
                                Reminder(
                                    id = UUID.randomUUID().toString(),
                                    title = _state.value.title,
                                    description = _state.value.description,
                                    time = _state.value.startDateTime,
                                    remindAt = desiredAlarmDate
                                )
                            }
                            TASK -> {
                                Task(
                                    id = UUID.randomUUID().toString(),
                                    title = _state.value.title,
                                    description = _state.value.description,
                                    time = _state.value.startDateTime,
                                    isDone = (_state.value.details as TaskDetails).isCompleted,
                                    remindAt = desiredAlarmDate
                                )
                            }
                            EVENT -> {
                                Event
                            }
                        }
                            .also {
                                Log.d("AgendaItemViewModel", "alarm date  ${it.alarmDate}")
                            }
                        val response = when (data) {
                            is Reminder -> reminderRepository.insertReminder(
                                data
                            )
                            is Task -> taskRepository.insertTask(
                               data
                            )

                            Event -> {
                                //TODO: Implement update event
                                Result.Success(Unit)
                            }
                        }
                        response
                            .onSuccess {
                                alarmScheduler.scheduleAlarm(data)
                                eventChannel.send(AgendaItemEvent.Created)
                            }.onError {
                                eventChannel.send(AgendaItemEvent.CreationScheduled)
                            }
                    }
                }
                Close -> Unit
                DismissDateDialog -> {
                    updateState {
                        it.copy(
                            isSelectDateDialog = false
                        )
                    }
                }
                DismissTimeDialog -> {

                    updateState {
                        it.copy(
                            isSelectTimeDialogVisible = false
                        )
                    }
                }
                ShowDateDialog -> {
                    updateState {
                        it.copy(
                            isSelectDateDialog = true
                        )
                    }
                }
                ShowTimeDialog -> {
                    updateState {
                        it.copy(
                            isSelectTimeDialogVisible = true
                        )
                    }
                }

            is SelectAlarm -> {
                updateState {
                    it.copy(
                        alarm = action.alarm
                    )
                }
            }

            is UpdateField -> {
                if (action.key == AgendaItems.EDIT_FIELD_TITLE_KEY) {
                    updateState {
                        it.copy(
                            title = action.value
                        )
                    }
                } else {
                    updateState {
                        it.copy(
                            description = action.value
                        )
                    }
                }
            }
        }
    }

    }

    private fun updateState(update: (AgendaItemState) -> AgendaItemState) {
        _state.update { update(it) }
    }

    private fun calculateTimeAlarm():ZonedDateTime {
        val alarm = _state.value.alarm
        val startDateTime = _state.value.startDateTime
        val newDateTime = when (alarm) {
            MINUTES_TEN -> startDateTime.minusMinutes(10)
            MINUTES_THIRTY -> startDateTime.minusMinutes(30)
            HOUR_ONE -> startDateTime.minusHours(1)
            HOUR_SIX -> startDateTime.minusHours(6)
            DAY_ONE -> startDateTime.minusDays(1)
        }
        return newDateTime.withSecond(0)
    }
}