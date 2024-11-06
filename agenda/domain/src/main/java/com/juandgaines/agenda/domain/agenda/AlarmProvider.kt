package com.juandgaines.agenda.domain.agenda

import kotlinx.coroutines.flow.StateFlow

interface AlarmProvider {
    fun  register()
    fun alarmAvailable (): StateFlow<Boolean>
    fun unregister()
}