package com.juandgaines.agenda.componets.selector_date

data class SelectorDayData(
    val initialDayOfWeek:Char,
    val dayOfMonth: Int,
    val isSelected: Boolean,
    val dayTime:Long
)