package com.juandgaines.agenda.data.agenda.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.juandgaines.agenda.domain.agenda.AgendaRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver:BroadcastReceiver() {
    @Inject
    lateinit var agendaRepository: AgendaRepository

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            // Do something
        }
    }
}