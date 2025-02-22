@file:OptIn(ExperimentalCoroutinesApi::class)

package com.juandgaines.agenda.presentation.agenda_item


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.juandgaines.agenda.domain.agenda.AgendaItems
import com.juandgaines.agenda.domain.agenda.AgendaItems.Event
import com.juandgaines.agenda.domain.agenda.AgendaItems.Reminder
import com.juandgaines.agenda.domain.agenda.AgendaItems.Task
import com.juandgaines.agenda.domain.agenda.AlarmScheduler
import com.juandgaines.agenda.domain.agenda.AttendeeMinimal
import com.juandgaines.agenda.domain.agenda.FileCompressor
import com.juandgaines.agenda.domain.event.AttendeeRepository
import com.juandgaines.agenda.domain.event.EventRepository
import com.juandgaines.agenda.domain.reminder.ReminderRepository
import com.juandgaines.agenda.domain.task.TaskRepository
import com.juandgaines.agenda.domain.utils.isToday
import com.juandgaines.agenda.domain.utils.toUtcLocalDateTime
import com.juandgaines.agenda.domain.utils.toZonedDateTime
import com.juandgaines.agenda.presentation.R
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemAction.AddEmailAsAttendee
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemAction.AddPicture
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemAction.Close
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemAction.Delete
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemAction.DismissAttendeeDialog
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemAction.DismissDateDialog
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemAction.DismissTimeDialog
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemAction.Edit
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemAction.EditField
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemAction.Join
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemAction.Leave
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemAction.RemoveAttendee
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemAction.Save
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemAction.SelectAlarm
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemAction.SelectAttendeeFilter
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemAction.SelectDateFinish
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemAction.SelectDateStart
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemAction.SelectTimeFinish
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemAction.SelectTimeStart
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemAction.ShowAttendeeDialog
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemAction.ShowDateDialog
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemAction.ShowTimeDialog
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemAction.UpdateField
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemDetailsUi.EventDetails
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemDetailsUi.TaskDetails
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemEvent.Created
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemEvent.CreationScheduled
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemEvent.Deleted
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemEvent.DeletionScheduled
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemEvent.Error
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemEvent.Joined
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemEvent.Left
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemEvent.UpdateScheduled
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemEvent.Updated
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemEvent.UserAdded
import com.juandgaines.agenda.presentation.agenda_item.AlarmOptions.DAY_ONE
import com.juandgaines.agenda.presentation.agenda_item.AlarmOptions.HOUR_ONE
import com.juandgaines.agenda.presentation.agenda_item.AlarmOptions.HOUR_SIX
import com.juandgaines.agenda.presentation.agenda_item.AlarmOptions.MINUTES_TEN
import com.juandgaines.agenda.presentation.agenda_item.AlarmOptions.MINUTES_THIRTY
import com.juandgaines.agenda.presentation.agenda_item.components.attendee.AttendeeUi
import com.juandgaines.agenda.presentation.agenda_item.components.attendee.UserInitialsFormatter
import com.juandgaines.core.domain.agenda.AgendaItemOption
import com.juandgaines.core.domain.agenda.AgendaItemOption.EVENT
import com.juandgaines.core.domain.agenda.AgendaItemOption.REMINDER
import com.juandgaines.core.domain.agenda.AgendaItemOption.TASK
import com.juandgaines.core.domain.auth.PatternValidator
import com.juandgaines.core.domain.network.ConnectivityObserver
import com.juandgaines.core.domain.util.onError
import com.juandgaines.core.domain.util.onSuccess
import com.juandgaines.core.presentation.navigation.ScreenNav.AgendaItem
import com.juandgaines.core.presentation.ui.UiText.DynamicString
import com.juandgaines.core.presentation.ui.UiText.StringResource
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
import java.time.Duration
import java.time.LocalTime
import java.time.Period
import java.time.ZonedDateTime
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AgendaItemViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val taskRepository: TaskRepository,
    private val reminderRepository: ReminderRepository,
    private val eventRepository: EventRepository,
    private val alarmScheduler: AlarmScheduler,
    private val emailPatterValidator: PatternValidator,
    private val attendeeRepository: AttendeeRepository,
    private val connectivityObserver: ConnectivityObserver,
    private val fileCompressor: FileCompressor
):ViewModel() {

    private var eventChannel = Channel<AgendaItemEvent>()
    val events = eventChannel.receiveAsFlow()

    private val _state = MutableStateFlow(AgendaItemState())
    private var _isEditing:MutableStateFlow<Boolean> = MutableStateFlow(false)
    private var _navParameters=savedStateHandle.toRoute<AgendaItem>()
    private var _isInit: Boolean = false
    private val _type = AgendaItemOption.fromOrdinal( _navParameters.type)

    val state:StateFlow<AgendaItemState> = _state
        .onStart {
            if (!_isInit){
                initState()
                _isInit = true
            }
        }
        .combine(
            connectivityObserver.isConnected
        ){ state, isConnected->
            state.copy(
                details = updateDetailsIfEvent(state.details) { det->
                    det.copy(
                        isConnectedToInternet = isConnected
                    )
                }
            )
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
                EVENT -> eventRepository.getEventById(idItem)
            }.onSuccess { item ->

                updateState { event->
                    event.copy(
                        isNew = false,
                        title = item.title,
                        description = item.description,
                        details = item.agendaItemDetails.toAgendaItemDetailsUi(
                            emailPatterValidator,
                        ),
                        alarm = when  {
                            Duration.between(item.alarmDate, item.date).toMinutes() <= 10L -> MINUTES_TEN
                            Duration.between(item.alarmDate, item.date).toMinutes() in 11..30L -> MINUTES_THIRTY
                            Duration.between(item.alarmDate, item.date).toHours() == 1L -> HOUR_ONE
                            Duration.between(item.alarmDate, item.date).toHours() == 6L -> HOUR_SIX
                            else -> DAY_ONE
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
                        REMINDER -> AgendaItemDetailsUi.ReminderDetails
                        TASK -> AgendaItemDetailsUi.TaskDetails()
                        EVENT -> AgendaItemDetailsUi.EventDetails(
                            emailPatterValidator = emailPatterValidator,
                            isGoingUser = true
                        )
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
                is SelectDateFinish -> {

                    updateState {
                        it.copy(
                            details = updateDetailsIfEvent { details->
                                details.copy(
                                    finishDate = action.dateMillis
                                        .toUtcLocalDateTime()
                                        .toZonedDateTime(
                                            LocalTime.of(
                                                details.finishDate.hour,
                                                details.finishDate.minute
                                            )
                                        ),
                                )
                            } ,
                            isSelectDateDialog = false
                        )
                    }
                }
                is SelectTimeFinish -> {
                    val details = _state.value.details as EventDetails
                    updateState {
                        it.copy(
                            details = details.copy(
                                finishDate = details.finishDate
                                    .withHour(action.hour)
                                    .withMinute(action.minutes)
                            ),
                            isSelectTimeDialogVisible = false
                        )
                    }
                }
                Delete -> {
                    val agendaItemId = _navParameters.id
                    if (agendaItemId != null) {
                        alarmScheduler.cancelAlarm(agendaItemId)
                        val response = when (_type) {
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
                            EVENT -> eventRepository.deleteEvent(
                                Event(
                                    id = agendaItemId,
                                    title = _state.value.title,
                                    description = _state.value.description,
                                    time = _state.value.startDateTime,
                                    endTime = (_state.value.details as EventDetails).finishDate,
                                    remindAt = _state.value.startDateTime,
                                    host = (_state.value.details as EventDetails).host,
                                    isUserEventCreator = (_state.value.details as EventDetails).isUserCreator,
                                    isGoing = (_state.value.details as EventDetails).isGoingUser,
                                )
                            )
                        }

                        response
                            .onSuccess {
                                eventChannel.send(Deleted)
                            }.onError {
                                eventChannel.send(
                                    DeletionScheduled
                                )
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
                                val compressedPhotos = fileCompressor.compressLocalFiles((_state.value.details as EventDetails).localPhotos.map { it.toString() })
                                Event(
                                    id = agendaItemId,
                                    title = _state.value.title,
                                    description = _state.value.description,
                                    time = _state.value.startDateTime,
                                    endTime = (_state.value.details as EventDetails).finishDate,
                                    attendee = (_state.value.details as EventDetails).attendees.map {
                                        AttendeeMinimal(
                                            email = it.email,
                                            fullName = it.fullName,
                                            userId = it.userId,
                                            isGoing = it.isGoing,
                                            isUserCreator = it.isUserCreator
                                        )
                                    },
                                    remindAt = desiredAlarmDate,
                                    host = (_state.value.details as EventDetails).host,
                                    isUserEventCreator = (_state.value.details as EventDetails).isUserCreator,
                                    isGoing = (_state.value.details as EventDetails).isGoingUser,
                                    localPhotos = compressedPhotos.listFiles
                                )
                            }
                        }

                        val response = when (data) {
                            is Reminder -> reminderRepository.updateReminder(
                                data
                            )
                            is Task -> taskRepository.updateTask(
                                data
                            )
                            is Event -> eventRepository.updateEvent(
                                data,
                                data.localPhotos
                            )
                        }
                        response
                            .onSuccess {
                                alarmScheduler.cancelAlarm(agendaItemId)

                                alarmScheduler.scheduleAlarm(data)
                                eventChannel.send(Updated)
                            }.onError {
                                eventChannel.send(UpdateScheduled)
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
                                val compressPhoto = fileCompressor.compressLocalFiles((_state.value.details as EventDetails).localPhotos.map { it.toString() })
                                Event(
                                    id = UUID.randomUUID().toString(),
                                    title = _state.value.title,
                                    description = _state.value.description,
                                    time = _state.value.startDateTime,
                                    endTime = (_state.value.details as EventDetails).finishDate,
                                    remindAt = desiredAlarmDate,
                                    attendee = (_state.value.details as EventDetails).attendees.map {
                                        AttendeeMinimal(
                                            email = it.email,
                                            fullName = it.fullName,
                                            userId = it.userId,
                                            isGoing = it.isGoing,
                                            isUserCreator = it.isUserCreator
                                        )
                                    },
                                    host = (_state.value.details as EventDetails).host,
                                    isGoing = (_state.value.details as EventDetails).isGoingUser,
                                    isUserEventCreator = (_state.value.details as EventDetails).isUserCreator,
                                    localPhotos = compressPhoto.listFiles
                                )
                            }
                        }
                        val response = when (data) {
                            is Reminder -> reminderRepository.insertReminder(
                                data
                            )
                            is Task -> taskRepository.insertTask(
                                data
                            )

                            is Event -> eventRepository.insertEvent(
                                data,
                                data.localPhotos
                            )
                        }
                        response
                            .onSuccess {
                                alarmScheduler.scheduleAlarm(data)
                                eventChannel.send(Created)
                            }.onError {
                                eventChannel.send(CreationScheduled)
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
                is ShowDateDialog -> {
                    updateState {
                        it.copy(
                            isSelectDateDialog = true,
                            isEditingEndDate = action.isEndDate
                        )
                    }
                }
                is ShowTimeDialog -> {
                    updateState {
                        it.copy(
                            isSelectTimeDialogVisible = true,
                            isEditingEndDate = action.isEndDate
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

                is SelectAttendeeFilter -> {
                    updateState {
                        it.copy(
                            attendeeFilter = action.filter
                        )
                    }
                }

                is AddEmailAsAttendee ->{

                    updateState { state->
                        state.copy(
                            details = updateDetailsIfEvent { d->
                                d.copy(
                                    isAddingVisitor = true
                                )
                            }
                        )
                    }
                        attendeeRepository.getAttendeeByEmail(action.email)
                            .onSuccess { attendee->
                                if (attendee != null){
                                    updateState { state->
                                        state.copy(
                                            details = updateDetailsIfEvent { d->
                                                d.copy(
                                                    attendees = d.attendees + AttendeeUi(
                                                        email = attendee.email,
                                                        fullName = attendee.fullName,
                                                        isGoing = true,
                                                        isUserCreator = false,
                                                        initials = UserInitialsFormatter.format(attendee.fullName ),
                                                        userId = attendee.userId,
                                                        eventId = _navParameters.id ?: "",
                                                        isCreator = false
                                                    ),
                                                    isAddAttendeeDialogVisible = false,
                                                    isAddingVisitor = false,
                                                    doesEmailExist = false
                                                )
                                            }
                                        )
                                    }

                                    eventChannel.send(
                                        UserAdded(
                                        DynamicString(action.email)
                                    )
                                    )
                                }
                                else{
                                    eventChannel.send(
                                        Error(
                                        StringResource(R.string.user_not_found)
                                    )
                                    )
                                    updateState {s ->
                                        s.copy(
                                            details = updateDetailsIfEvent { d->
                                                d.copy(
                                                    doesEmailExist = true,
                                                    isAddingVisitor = false
                                                )
                                            }
                                        )
                                    }
                                }

                            }

                }
                is RemoveAttendee -> {
                    updateState { state->
                        state.copy(
                            details = updateDetailsIfEvent { d->
                                d.copy(
                                    attendees = d.attendees.filter { it.userId != action.attendeeId }
                                )
                            }
                        )
                    }
                }
                ShowAttendeeDialog -> {

                    updateState {s->
                        s.copy(
                            details = updateDetailsIfEvent { d->
                                d.copy(
                                    isAddAttendeeDialogVisible = true
                                )
                            }
                        )
                    }

                }

                DismissAttendeeDialog -> {
                    updateState {s->
                        s.copy(
                            details = updateDetailsIfEvent { d->
                                d.copy(
                                    isAddAttendeeDialogVisible = false
                                )
                            }
                        )
                    }
                }

                Leave -> {
                    val agendaItemId = _navParameters.id
                    agendaItemId?.let {
                        val desiredAlarmDate = calculateTimeAlarm()
                        val event = Event(
                            id = _navParameters.id!!,
                            title = _state.value.title,
                            description = _state.value.description,
                            time = _state.value.startDateTime,
                            endTime = (_state.value.details as EventDetails).finishDate,
                            attendee = (_state.value.details as EventDetails).attendees.map {
                                AttendeeMinimal(
                                    email = it.email,
                                    fullName = it.fullName,
                                    userId = it.userId,
                                    isGoing = it.isGoing,
                                    isUserCreator = it.isUserCreator
                                )
                            },
                            remindAt = desiredAlarmDate,
                            host = (_state.value.details as EventDetails).host,
                            isUserEventCreator = (_state.value.details as EventDetails).isUserCreator,
                            isGoing = false
                        )
                        eventRepository.updateEvent(event, emptyList())
                            .onSuccess {
                                eventChannel.send(Left)
                            }.onError {
                                eventChannel.send(Error(StringResource(R.string.error_leaving_event)))
                            }
                    }?:run {
                        eventChannel.send(Error(StringResource(R.string.error_leaving_event)))
                    }
                }

                Join -> {
                    val agendaItemId = _navParameters.id
                    agendaItemId?.let {
                        val desiredAlarmDate = calculateTimeAlarm()
                        val event = Event(
                            id = _navParameters.id!!,
                            title = _state.value.title,
                            description = _state.value.description,
                            time = _state.value.startDateTime,
                            endTime = (_state.value.details as EventDetails).finishDate,
                            attendee = (_state.value.details as EventDetails).attendees.map {
                                AttendeeMinimal(
                                    email = it.email,
                                    fullName = it.fullName,
                                    userId = it.userId,
                                    isGoing = it.isGoing,
                                    isUserCreator = it.isUserCreator
                                )
                            },
                            remindAt = desiredAlarmDate,
                            host = (_state.value.details as EventDetails).host,
                            isUserEventCreator = (_state.value.details as EventDetails).isUserCreator,
                            isGoing = true
                        )
                        eventRepository.updateEvent(event, emptyList())
                            .onSuccess {
                                eventChannel.send(Joined)
                            }.onError {
                                eventChannel.send(Error(StringResource(R.string.error_joining_event)))
                            }
                    }?:run {
                        eventChannel.send(Error(StringResource(R.string.error_joining_event)))
                    }

                }

                is AddPicture -> {
                    updateState {
                        it.copy(
                            details = updateDetailsIfEvent { d->
                                d.copy(
                                    localPhotos = d.localPhotos + action.uri
                                )
                            }
                        )
                    }
                }
            }
        }

    }

    private fun updateState(update: (AgendaItemState) -> AgendaItemState) {
        _state.update { update(it) }
    }

    private fun updateDetailsIfEvent(stateParam:AgendaItemDetailsUi? = null,update: (AgendaItemDetailsUi.EventDetails) -> AgendaItemDetailsUi.EventDetails): AgendaItemDetailsUi {
        return when (val details = stateParam?:state.value.details) {
            is AgendaItemDetailsUi.EventDetails -> update(details)
            else -> details
        }
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