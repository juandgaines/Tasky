package com.juandgaines.agenda.data.agenda

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
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
            putExtra(AlarmReceiver.TITLE, agendaItem.title)
            putExtra(AlarmReceiver.DESCRIPTION, agendaItem.description)
            putExtra(AlarmReceiver.AGENDA_ITEM_ID, agendaItem.id)
            putExtra(AlarmReceiver.TIME, agendaItem.date.toEpochMilli())
        }

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
                agendaItem.hashCode(),
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}