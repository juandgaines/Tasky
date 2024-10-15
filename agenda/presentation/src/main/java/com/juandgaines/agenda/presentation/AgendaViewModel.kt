@file:Suppress("OPT_IN_USAGE")

package com.juandgaines.agenda.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juandgaines.agenda.domain.agenda.AgendaRepository
import com.juandgaines.agenda.domain.agenda.InitialsCalculator
import com.juandgaines.agenda.domain.utils.endOfDay
import com.juandgaines.agenda.domain.utils.startOfDay
import com.juandgaines.agenda.domain.utils.toLocalDateWithZoneId
import com.juandgaines.agenda.domain.utils.toUtcLocalDateTime
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
import com.juandgaines.agenda.presentation.AgendaState.Companion.calculateRangeDays
import com.juandgaines.core.domain.auth.AuthCoreService
import com.juandgaines.core.domain.util.Result.Error
import com.juandgaines.core.domain.util.Result.Success
import com.juandgaines.core.presentation.ui.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class AgendaViewModel @Inject constructor(
    private val initialsCalculator: InitialsCalculator,
    private val authCoreService: AuthCoreService,
    private val agendaRepository: AgendaRepository
):ViewModel() {


    var state by mutableStateOf(AgendaState())
        private set

    private val eventChannel = Channel<AgendaEvents>()
    val events = eventChannel.receiveAsFlow()

    private val _time = MutableStateFlow(state.selectedLocalDate)

    init {
        viewModelScope.launch {
            initialsCalculator.getInitials().let {
                state = state.copy(userInitials = it)
            }
        }

        _time.flatMapLatest {
            agendaRepository.getItems(
                it.startOfDay(),
                it.endOfDay()
            )
        }.onEach {
            state = state.copy(agendaItems = it)
        }.launchIn(viewModelScope)
    }

    fun onAction(action: AgendaActions) {
        viewModelScope.launch {
            when (action) {
                is SelectDate -> {

                    val utcZonedDateTime = action.date.toUtcLocalDateTime()
                    val newDate = utcZonedDateTime.toLocalDateWithZoneId(
                        ZoneId.systemDefault()
                    )
                    _time.value = newDate
                    state = state.copy(
                        selectedLocalDate = newDate,
                        isDatePickerOpened = false,
                        dateRange = calculateRangeDays(newDate)
                    )
                }
                is SelectDateWithingRange -> {
                    val date = action.date
                    _time.value = date
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
                            eventChannel.send(AgendaEvents.LogOut)
                        }
                        is Error -> {
                            eventChannel.send(AgendaEvents.Error(result.error.asUiText()))
                        }
                    }
                    state = state.copy(isLoading = false)
                }
            }
        }

    }
}