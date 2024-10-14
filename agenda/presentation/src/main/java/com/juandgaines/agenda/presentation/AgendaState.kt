package com.juandgaines.agenda.presentation

import com.juandgaines.agenda.componets.selector_date.SelectorDayData
import java.time.ZonedDateTime

data class AgendaState(
    val labelDate:String = "",
    val isDatePickerOpened:Boolean = false,
    val selectedDate:ZonedDateTime = ZonedDateTime.now(),
    val dateRange: List<SelectorDayData> = calculateRangeDays(selectedDate),
){
    val currentMonth:String
        get() = selectedDate.month.name.uppercase()

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