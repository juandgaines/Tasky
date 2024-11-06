@file:Suppress("OPT_IN_USAGE")

package com.juandgaines.agenda.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juandgaines.agenda.domain.agenda.AgendaItems.Event
import com.juandgaines.agenda.domain.agenda.AgendaItems.Reminder
import com.juandgaines.agenda.domain.agenda.AgendaItems.Task
import com.juandgaines.agenda.domain.agenda.AgendaRepository
import com.juandgaines.agenda.domain.agenda.AgendaSyncOperations
import com.juandgaines.agenda.domain.agenda.AgendaSyncScheduler
import com.juandgaines.agenda.domain.agenda.InitialsCalculator
import com.juandgaines.agenda.domain.reminder.ReminderRepository
import com.juandgaines.agenda.domain.task.TaskRepository
import com.juandgaines.agenda.domain.utils.endOfDay
import com.juandgaines.agenda.domain.utils.startOfDay
import com.juandgaines.agenda.domain.utils.toEpochMilli
import com.juandgaines.agenda.domain.utils.toLocalDateWithZoneId
import com.juandgaines.agenda.domain.utils.toUtcLocalDateTime
import com.juandgaines.agenda.presentation.R
import com.juandgaines.agenda.presentation.home.AgendaActions.AgendaOperation
import com.juandgaines.agenda.presentation.home.AgendaActions.CreateItem
import com.juandgaines.agenda.presentation.home.AgendaActions.DismissCreateContextMenu
import com.juandgaines.agenda.presentation.home.AgendaActions.DismissDateDialog
import com.juandgaines.agenda.presentation.home.AgendaActions.DismissProfileMenu
import com.juandgaines.agenda.presentation.home.AgendaActions.Logout
import com.juandgaines.agenda.presentation.home.AgendaActions.SelectDate
import com.juandgaines.agenda.presentation.home.AgendaActions.SelectDateWithingRange
import com.juandgaines.agenda.presentation.home.AgendaActions.SendNotificationPermission
import com.juandgaines.agenda.presentation.home.AgendaActions.SendScheduleAlarmPermission
import com.juandgaines.agenda.presentation.home.AgendaActions.ShowCreateContextMenu
import com.juandgaines.agenda.presentation.home.AgendaActions.ShowDateDialog
import com.juandgaines.agenda.presentation.home.AgendaActions.ShowProfileMenu
import com.juandgaines.agenda.presentation.home.AgendaActions.ToggleDoneTask
import com.juandgaines.agenda.presentation.home.AgendaCardMenuOperations.Delete
import com.juandgaines.agenda.presentation.home.AgendaCardMenuOperations.Edit
import com.juandgaines.agenda.presentation.home.AgendaCardMenuOperations.Open
import com.juandgaines.agenda.presentation.home.AgendaEvents.GoToItemScreen
import com.juandgaines.agenda.presentation.home.AgendaEvents.LogOut
import com.juandgaines.agenda.presentation.home.AgendaItemUi.Item
import com.juandgaines.agenda.presentation.home.AgendaItemUi.Needle
import com.juandgaines.agenda.presentation.home.AgendaState.Companion.calculateRangeDays
import com.juandgaines.core.domain.auth.AuthCoreService
import com.juandgaines.core.domain.util.Result.Error
import com.juandgaines.core.domain.util.Result.Success
import com.juandgaines.core.domain.util.onError
import com.juandgaines.core.domain.util.onSuccess
import com.juandgaines.core.domain.agenda.AgendaItemOption.EVENT
import com.juandgaines.core.domain.agenda.AgendaItemOption.REMINDER
import com.juandgaines.core.domain.agenda.AgendaItemOption.TASK
import com.juandgaines.core.presentation.ui.UiText.StringResource
import com.juandgaines.core.presentation.ui.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes

@HiltViewModel
class AgendaViewModel @Inject constructor(
    private val initialsCalculator: InitialsCalculator,
    private val authCoreService: AuthCoreService,
    private val agendaRepository: AgendaRepository,
    private val taskRepository: TaskRepository,
    private val reminderRepository: ReminderRepository,
    private val agendaSyncScheduler: AgendaSyncScheduler
):ViewModel() {

    private val _state = MutableStateFlow(AgendaState())

    private val eventChannel = Channel<AgendaEvents>()
    val events = eventChannel.receiveAsFlow()


    private fun updateState(update: (AgendaState) -> AgendaState) {
        _state.update { update(it) }
    }
    private val _selectedDate = MutableStateFlow(LocalDate.now())
    private var _isInit:Boolean = false

    val state = _state
        .onStart {
            if (!_isInit) {
                _isInit = true
                initialsCalculator.getInitials().let { initials ->
                    updateState {
                        it.copy(userInitials = initials)
                    }

                }
                agendaSyncScheduler.scheduleSync(
                    AgendaSyncOperations.FetchAgendas(
                        30.minutes
                    )
                )
                agendaRepository.syncPendingAgendaItem()
            }
        }
        .combine(_selectedDate){ state, selectedDate->
            agendaRepository.fetchItems(selectedDate.toEpochMilli())
            state.copy(selectedLocalDate = selectedDate)
        }
        .flatMapLatest { state->
            agendaRepository.getItems(
                state.selectedLocalDate.startOfDay(),
                state.selectedLocalDate.endOfDay()
        )
    }.map {agendaItems->
        agendaItems
            .map {
                Item(it)
            }
            .plus(
                Needle()
            )
            .sortedBy {
                it.date
            }
    }.map {
        _state.value.copy(agendaItems = it)
        }.stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = AgendaState()
        )

    fun onAction(action: AgendaActions) {
        viewModelScope.launch {
            when (action) {
                is SelectDate -> {

                    val utcZonedDateTime = action.date.toUtcLocalDateTime()
                    val newDate = utcZonedDateTime.toLocalDateWithZoneId(
                        ZoneId.systemDefault()
                    )
                    _selectedDate.value = newDate
                    updateState {
                        it.copy(
                            selectedLocalDate = newDate,
                            isDatePickerOpened = false,
                            dateRange = calculateRangeDays(newDate)
                        )
                    }
                }
                is SelectDateWithingRange -> {
                    val date = action.date
                    _selectedDate.value = date
                    val range = _state.value.dateRange.map {
                        it.copy(isSelected = it.dayTime == date)
                    }
                    updateState {
                        it.copy(dateRange = range)
                    }
                }
                ShowProfileMenu -> {
                    updateState {
                        it.copy(isProfileMenuVisible = true)
                    }
                }

                DismissProfileMenu -> {
                    updateState {
                        it.copy(isProfileMenuVisible = false)
                    }
                }

                DismissDateDialog -> {
                    updateState {
                        it.copy(isDatePickerOpened = false)
                    }
                }
                ShowDateDialog -> {
                    updateState {
                        it.copy(isDatePickerOpened = true)
                    }
                }

                ShowCreateContextMenu ->{
                    updateState {
                        it.copy(isCreateContextMenuVisible = true)
                    }
                }

                DismissCreateContextMenu ->
                    updateState {
                        it.copy(isCreateContextMenuVisible = false)
                    }

                Logout ->{
                    updateState {
                        it.copy(isLoading = true)
                    }
                    when(val result = authCoreService.logout()){
                        is Success -> {
                            eventChannel.send(LogOut)
                        }
                        is Error -> {
                            eventChannel.send(AgendaEvents.Error(result.error.asUiText()))
                        }
                    }
                    updateState {
                        it.copy(isLoading = false)
                    }
                }

                is AgendaOperation -> {
                    when(action.agendaOperation){
                        is Delete -> {
                            when (val agendaItem = action.agendaOperation.agendaItem){
                                is Task -> {
                                   taskRepository.deleteTask(agendaItem)
                                       .onSuccess {
                                             eventChannel.send(
                                                 AgendaEvents.Success(
                                                     StringResource(R.string.task_deleted)
                                                 )
                                             )
                                       }.onError {

                                            eventChannel.send(AgendaEvents.Error(it.asUiText()))
                                       }
                                }
                                is Reminder -> {
                                    reminderRepository.deleteReminder(agendaItem)
                                        .onSuccess {
                                            eventChannel.send(
                                                AgendaEvents.Success(
                                                    StringResource(R.string.reminder_deleted)
                                                )
                                            )
                                        }.onError {
                                            eventChannel.send(AgendaEvents.Error(it.asUiText()))
                                        }
                                }
                                is Event -> {

                                }
                            }
                        }
                        is Edit -> {
                            val item = action.agendaOperation.agendaItem
                            val type =  when (item) {
                                is Task -> {
                                    TASK
                                }
                                is Reminder -> {
                                    REMINDER
                                }
                                is Event -> {
                                    EVENT
                                }
                            }
                            eventChannel.send(
                                GoToItemScreen(item.id, type, true)
                            )
                        }
                        is Open -> {
                            val item = action.agendaOperation.agendaItem
                            val type =  when (item) {
                                is Task -> {
                                    TASK
                                }
                                is Reminder -> {
                                    REMINDER
                                }
                                is Event -> {
                                    EVENT
                                }
                            }
                            eventChannel.send(
                                GoToItemScreen(item.id, type, false)
                            )
                        }
                    }
                }

                is ToggleDoneTask -> {
                    val task = action.task
                    taskRepository.updateTask(task.copy(isDone = !task.isDone))
                        .onSuccess {
                            eventChannel.send(
                                AgendaEvents.Success(StringResource(R.string.task_updated))
                            )
                        }.onError {
                            eventChannel.send(AgendaEvents.Error(it.asUiText()))
                        }
                }

                is CreateItem -> {
                    eventChannel.send(
                        GoToItemScreen(
                            type = action.option,
                            isEditing = true,
                            dateEpochMilli = _selectedDate.value.toEpochMilli()
                        )
                    )
                }

                is SendNotificationPermission -> {
                    updateState {
                        it.copy(
                            isNotificationAccepted = action.permission,
                            isNotificationRationaleNeeded = action.needRationale
                        )
                    }

                }
                is SendScheduleAlarmPermission -> {
                    updateState {
                        it.copy(
                            isScheduleAlarmPermissionAccepted = action.permission,
                        )
                    }
                }
            }
        }
    }

}