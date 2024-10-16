package com.juandgaines.agenda.componets.selector_date

import java.time.LocalDate

data class SelectorDayData(
    val initialDayOfWeek:Char,
    val dayOfMonth: Int,
    val isSelected: Boolean,
    val dayTime:LocalDate
)