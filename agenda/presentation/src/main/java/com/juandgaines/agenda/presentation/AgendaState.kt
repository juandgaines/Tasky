package com.juandgaines.agenda.presentation

import com.juandgaines.agenda.componets.selector_date.SelectorDayData
import com.juandgaines.agenda.domain.utils.toFormattedDate
import java.time.ZonedDateTime

data class AgendaState(
    val labelDate:String = "",
    val isDatePickerOpened:Boolean = false,
    val selectedLocalDate:ZonedDateTime = ZonedDateTime.now(),
    val dateRange: List<SelectorDayData> = calculateRangeDays(selectedLocalDate),
    val userInitials:String = "",
    val isCreateContextMenuVisible : Boolean = false,
    val isProfileMenuVisible : Boolean = false,
){
    val currentMonth:String
        get() = selectedLocalDate.month.name.uppercase()

    val labelDateRange:String
        get() = dateRange.first{it.isSelected}.dayTime.toFormattedDate()

    companion object{

        fun calculateRangeDays(date:ZonedDateTime):List<SelectorDayData>{
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