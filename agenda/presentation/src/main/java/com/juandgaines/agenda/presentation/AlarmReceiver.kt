package com.juandgaines.agenda.presentation

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(
        context: Context?,
        intent: Intent?,
    ) {
        //TODO: Remove log and implement logic with notification
        Log.d("AlarmReceiver", "Alarm received")
    }
}