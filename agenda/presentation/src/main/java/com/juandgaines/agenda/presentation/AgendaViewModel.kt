@file:Suppress("OPT_IN_USAGE")

package com.juandgaines.agenda.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juandgaines.agenda.domain.agenda.AgendaRepository
import com.juandgaines.agenda.domain.agenda.AgendaSyncOperations
import com.juandgaines.agenda.domain.agenda.AgendaSyncScheduler
import com.juandgaines.agenda.domain.agenda.InitialsCalculator
import com.juandgaines.agenda.domain.reminder.Reminder
import com.juandgaines.agenda.domain.task.Task
import com.juandgaines.agenda.domain.task.TaskRepository
import com.juandgaines.agenda.domain.utils.endOfDay
import com.juandgaines.agenda.domain.utils.startOfDay
import com.juandgaines.agenda.domain.utils.toEpochMilli
import com.juandgaines.agenda.domain.utils.toLocalDateWithZoneId
import com.juandgaines.agenda.domain.utils.toUtcLocalDateTime
import com.juandgaines.agenda.presentation.AgendaActions.AgendaOperation
import com.juandgaines.agenda.presentation.AgendaActions.CreateItem
import com.juandgaines.agenda.presentation.AgendaActions.DismissCreateContextMenu
import com.juandgaines.agenda.presentation.AgendaActions.DismissDateDialog
import com.juandgaines.agenda.presentation.AgendaActions.DismissProfileMenu
import com.juandgaines.agenda.presentation.AgendaActions.Logout
import com.juandgaines.agenda.presentation.AgendaActions.SelectDate
import com.juandgaines.agenda.presentation.AgendaActions.SelectDateWithingRange
import com.juandgaines.agenda.presentation.AgendaActions.ShowCreateContextMenu
import com.juandgaines.agenda.presentation.AgendaActions.ShowDateDialog
import com.juandgaines.agenda.presentation.AgendaActions.ShowProfileMenu
import com.juandgaines.agenda.presentation.AgendaActions.ToggleDoneTask
import com.juandgaines.agenda.presentation.AgendaCardMenuOperations.Delete
import com.juandgaines.agenda.presentation.AgendaCardMenuOperations.Edit
import com.juandgaines.agenda.presentation.AgendaCardMenuOperations.Open
import com.juandgaines.agenda.presentation.AgendaEvents.LogOut
import com.juandgaines.agenda.presentation.AgendaState.Companion.calculateRangeDays
import com.juandgaines.core.domain.auth.AuthCoreService
import com.juandgaines.core.domain.util.Result.Error
import com.juandgaines.core.domain.util.Result.Success
import com.juandgaines.core.domain.util.onError
import com.juandgaines.core.domain.util.onSuccess
import com.juandgaines.core.presentation.ui.UiText
import com.juandgaines.core.presentation.ui.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.UUID
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes

@HiltViewModel
class AgendaViewModel @Inject constructor(
    private val initialsCalculator: InitialsCalculator,
    private val authCoreService: AuthCoreService,
    private val agendaRepository: AgendaRepository,
    private val taskRepository: TaskRepository,
    private val agendaSyncScheduler: AgendaSyncScheduler
):ViewModel() {


    var state by mutableStateOf(AgendaState())
        private set

    private val eventChannel = Channel<AgendaEvents>()
    val events = eventChannel.receiveAsFlow()

    private val _selectedDate = MutableStateFlow(state.selectedLocalDate)

    init {
        viewModelScope.launch {
            agendaSyncScheduler.scheduleSync(
                AgendaSyncOperations.FetchAgendas(
                    30.minutes
                )
            )
        }
        viewModelScope.launch {
            initialsCalculator.getInitials().let {
                state = state.copy(userInitials = it)
                agendaRepository.fetchItems(_selectedDate.value.toEpochMilli())
            }
        }

        viewModelScope.launch {
            agendaRepository.syncPendingAgendaItem()
        }

        _selectedDate.flatMapLatest { date->
            agendaRepository.getItems(
                date.startOfDay(),
                date.endOfDay()
            )
        }.map {agendaItems->
            agendaItems
                .map {
                    AgendaItemUi.Item(it)
                }
                .plus(
                    AgendaItemUi.Needle()
                )
                .sortedBy {
                    it.date
                }
        }.onEach { agendaItems ->
            state = state.copy(agendaItems = agendaItems)
        }.launchIn(viewModelScope)

        viewModelScope.launch {

            taskRepository.insertTask(
                Task(
                    id = UUID.randomUUID().toString(),
                    title = "Task 4",
                    description = "Description 4",
                    time = ZonedDateTime.now().plusHours(1),
                    remindAt = ZonedDateTime.now().plusMinutes(30),
                    isDone = false
                )
            )

        }
    }

    fun onAction(action: AgendaActions) {
        viewModelScope.launch {
            when (action) {
                is SelectDate -> {

                    val utcZonedDateTime = action.date.toUtcLocalDateTime()
                    val newDate = utcZonedDateTime.toLocalDateWithZoneId(
                        ZoneId.systemDefault()
                    )
                    _selectedDate.value = newDate
                    state = state.copy(
                        selectedLocalDate = newDate,
                        isDatePickerOpened = false,
                        dateRange = calculateRangeDays(newDate)
                    )
                }
                is SelectDateWithingRange -> {
                    val date = action.date
                    _selectedDate.value = date
                    val range = state.dateRange.map {
                        it.copy(isSelected = it.dayTime == date)
                    }
                    state = state.copy(dateRange = range)
                }

                ShowProfileMenu -> {
                    state = state.copy(isProfileMenuVisible = true)
                }

                DismissProfileMenu -> {
                    state = state.copy(isProfileMenuVisible = false)
                }

                DismissDateDialog -> {
                    state = state.copy(isDatePickerOpened = false)
                }
                ShowDateDialog -> {
                    state = state.copy(isDatePickerOpened = true)
                }

                ShowCreateContextMenu ->{
                    state = state.copy(isCreateContextMenuVisible = true)
                }

                DismissCreateContextMenu ->
                    state = state.copy(isCreateContextMenuVisible = false)

                is CreateItem -> {

                }

                Logout ->{
                    state = state.copy(isLoading = true)
                    when(val result = authCoreService.logout()){
                        is Success -> {
                            eventChannel.send(LogOut)
                        }
                        is Error -> {
                            eventChannel.send(AgendaEvents.Error(result.error.asUiText()))
                        }
                    }
                    state = state.copy(isLoading = false)
                }

                is AgendaOperation -> {
                    when(action.agendaOperation){
                        is Delete -> {
                            when (val agendaItem = action.agendaOperation.agendaItem){
                                is Task -> {
                                   taskRepository.deleteTask(agendaItem)
                                       .onSuccess {
                                             eventChannel.send(AgendaEvents.Success(UiText.StringResource(R.string.task_deleted)))
                                       }.onError {
                                            eventChannel.send(AgendaEvents.Error(it.asUiText()))
                                       }
                                }
                                is Reminder -> {

                                }
                            }
                        }
                        is Edit -> {

                        }
                        is Open -> {

                        }
                    }
                }

                is ToggleDoneTask -> {

                    val task = action.task
                    taskRepository.updateTask(task.copy(isDone = !task.isDone))
                        .onSuccess {
                            eventChannel.send(AgendaEvents.Success(UiText.StringResource(R.string.task_updated)))
                        }.onError {
                            eventChannel.send(AgendaEvents.Error(it.asUiText()))
                        }
                }
            }
        }
    }
}