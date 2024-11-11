package com.juandgaines.agenda.data.agenda.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.juandgaines.agenda.domain.agenda.AgendaRepository
import com.juandgaines.agenda.domain.agenda.AlarmScheduler
import com.juandgaines.agenda.domain.utils.toEpochMilli
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver:BroadcastReceiver() {

    @Inject
    lateinit var applicationScope : CoroutineScope

    @Inject
    lateinit var agendaRepository: AgendaRepository

    @Inject
    lateinit var alarmScheduler: AlarmScheduler

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            applicationScope.launch {
                val futureAgendaItems = agendaRepository.fetchFutureItems(
                    ZonedDateTime.now().toEpochMilli()
                )
                futureAgendaItems.forEach {
                    alarmScheduler.scheduleAlarm(it)
                }
            }
        }
    }
}