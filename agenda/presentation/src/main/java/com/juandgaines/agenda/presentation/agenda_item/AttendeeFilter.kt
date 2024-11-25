package com.juandgaines.agenda.presentation.agenda_item

import androidx.annotation.StringRes
import com.juandgaines.agenda.presentation.R

enum class AttendeeFilter(@StringRes val resource: Int) {
    ALL(R.string.all),
    GOING(R.string.going),
    NOT_GOING(R.string.not_going)
}