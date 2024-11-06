package com.juandgaines.agenda.data.agenda

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.juandgaines.agenda.domain.agenda.AgendaItems
import com.juandgaines.agenda.domain.agenda.AlarmScheduler
import javax.inject.Inject

class DefaultAlarmScheduler @Inject constructor(
    private val context: Context
):AlarmScheduler {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override fun scheduleAlarm(agendaItem: AgendaItems) {
        val intent = Intent(context, AlarmReceiver::class.java)
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            agendaItem.alarmDate.toInstant().toEpochMilli(),
            PendingIntent.getBroadcast(
                context,
                agendaItem.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    override fun cancelAlarm(agendaItem: AgendaItems) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                agendaItem.id.toInt(),
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}