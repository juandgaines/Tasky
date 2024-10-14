package com.juandgaines.agenda.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juandgaines.agenda.domain.agenda.InitialsCalculator
import com.juandgaines.agenda.domain.utils.toUtcZonedDateTime
import com.juandgaines.agenda.domain.utils.toZonedDateTimeWithZoneId
import com.juandgaines.agenda.presentation.AgendaActions.DismissDateDialog
import com.juandgaines.agenda.presentation.AgendaActions.SelectDate
import com.juandgaines.agenda.presentation.AgendaActions.SelectDateWithingRange
import com.juandgaines.agenda.presentation.AgendaActions.SelectProfile
import com.juandgaines.agenda.presentation.AgendaActions.ShowDateDialog
import com.juandgaines.agenda.presentation.AgendaState.Companion.calculateRangeDays
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class AgendaViewModel @Inject constructor(
    private val initialsCalculator: InitialsCalculator,
):ViewModel() {


    var state by mutableStateOf(AgendaState())
        private set

    private val eventChannel = Channel<AgendaEvents>()
    val events = eventChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            initialsCalculator.getInitials().let {
                state = state.copy(userInitials = it)
            }
        }
        state = state.copy()
    }

    fun onAction(action: AgendaActions) {
        when (action) {
            is SelectDate -> {

                val utcZonedDateTime = action.date.toUtcZonedDateTime()

                val newDate = utcZonedDateTime.toZonedDateTimeWithZoneId(
                    ZoneId.systemDefault()
                )

                state = state.copy(
                    selectedLocalDate = newDate,
                    isDatePickerOpened = false,
                    dateRange = calculateRangeDays(newDate)
                )
            }
            is SelectDateWithingRange -> {
                val date = action.date
                val range = state.dateRange.map {
                    it.copy(isSelected = it.dayTime == date)
                }
                state = state.copy(dateRange = range)
            }

            SelectProfile -> {

            }

            DismissDateDialog -> {
                state = state.copy(isDatePickerOpened = false)
            }
            ShowDateDialog -> {
                state = state.copy(isDatePickerOpened = true)
            }
        }
    }
}