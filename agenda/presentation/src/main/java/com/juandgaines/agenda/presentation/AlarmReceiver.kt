package com.juandgaines.agenda.presentation

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.juandgaines.agenda.domain.agenda.AlarmScheduler

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(
        context: Context?,
        intent: Intent?,
    ) {
        Log.d("AlarmReceiver", "Alarm received")
        if (intent != null) {
            val agendaItem = intent.getStringExtra(AlarmScheduler.EXTRA)
            Log.d("AlarmReceiver", "Alarm received for $agendaItem")
        }
    }
}