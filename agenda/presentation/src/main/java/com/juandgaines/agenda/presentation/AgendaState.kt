package com.juandgaines.agenda.presentation

import com.juandgaines.agenda.componets.selector_date.SelectorDayData
import java.time.ZonedDateTime

data class AgendaState(
    val labelDate:String = "",
    val isDatePickerOpened:Boolean = false,
    val selectedDate:ZonedDateTime = ZonedDateTime.now(),
    val dateRange: List<SelectorDayData> = calculateRangeDays(selectedDate),
    val userInitials:String = ""
){
    val currentMonth:String
        get() = selectedDate.month.name.uppercase()

    companion object{

        private const val ONE_NAME = 1
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

        fun calculateUserInitials(fullName:String):String{
            val nameParts = fullName.trim().split("\\s+".toRegex())

            return when (nameParts.size) {
                ONE_NAME -> {
                    nameParts[0].take(2).uppercase()
                }
                else -> {
                    val firstInitial = nameParts.first().first().uppercaseChar()
                    val lastInitial = nameParts.last().first().uppercaseChar()
                    "$firstInitial$lastInitial"
                }
            }
        }
    }
}