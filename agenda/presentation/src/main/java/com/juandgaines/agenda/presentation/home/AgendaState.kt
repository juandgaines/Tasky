package com.juandgaines.agenda.presentation.home

import com.juandgaines.agenda.presentation.home.componets.selector_date.SelectorDayData
import com.juandgaines.agenda.domain.utils.toFormattedDate
import java.time.LocalDate

data class AgendaState(
    val labelDate:String = "",
    val isDatePickerOpened:Boolean = false,
    val selectedLocalDate:LocalDate = LocalDate.now(),
    val dateRange: List<SelectorDayData> = calculateRangeDays(selectedLocalDate),
    val userInitials:String = "",
    val isCreateContextMenuVisible : Boolean = false,
    val isProfileMenuVisible : Boolean = false,
    val isLoading:Boolean = false,
    val agendaItems: List<AgendaItemUi> = emptyList(),
    val isNotificationAccepted: Boolean = false,
    val isNotificationRationaleNeeded: Boolean = false,
    val isScheduleAlarmPermissionAccepted: Boolean = true,
){
    val currentMonth:String
        get() = selectedLocalDate.month.name.uppercase()

    val labelDateRange:String
        get() = dateRange.first{it.isSelected}.dayTime.toFormattedDate()

    companion object{

        fun calculateRangeDays(date:LocalDate):List<SelectorDayData>{
            return (0..5).map { day ->
                val newDate = date.plusDays(day.toLong())
                SelectorDayData(
                    initialDayOfWeek = newDate.dayOfWeek.name.first(),
                    dayOfMonth = newDate.dayOfMonth,
                    isSelected = date == newDate,
                    dayTime = newDate
                )
            }
        }
    }
}