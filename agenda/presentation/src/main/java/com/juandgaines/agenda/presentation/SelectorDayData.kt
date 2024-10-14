package com.juandgaines.agenda.presentation

data class SelectorDayData(
    val initialDayOfWeek:Char,
    val dayOfMonth: Int,
    val isSelected: Boolean,
    val dayTime:Long
)