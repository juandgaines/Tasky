package com.juandgaines.agenda.data.agenda

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.juandgaines.agenda.domain.agenda.AgendaItems
import com.juandgaines.agenda.domain.agenda.AlarmScheduler
import com.juandgaines.agenda.domain.utils.toEpochMilli
import javax.inject.Inject

class DefaultAlarmScheduler @Inject constructor(
    private val context: Context
):AlarmScheduler {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override fun scheduleAlarm(agendaItem: AgendaItems) {

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(AlarmReceiver.NOTIFICATION_DATA, NotificationDataParcelable(
                id = agendaItem.id,
                title = agendaItem.title,
                type = agendaItem.agendaItemOption.ordinal,
                description = agendaItem.description,
                date = agendaItem.date.toEpochMilli()
            ))
        }

        Log.d("DefaultAlarmScheduler", "scheduleAlarm hashcode: ${agendaItem.hashCode()}")
        Log.d("DefaultAlarmScheduler", "scheduleAlarm hashcode: ${agendaItem.hashCode()}")

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
        Log.d("DefaultAlarmScheduler", "cancelAlarm hashcode: ${agendaItem.hashCode()}")
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                agendaItem.hashCode(),
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}