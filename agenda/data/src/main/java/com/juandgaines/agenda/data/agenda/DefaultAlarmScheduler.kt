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
            putExtra(AlarmReceiver.NOTIFICATION_DATA, NotificationDataParcelable(
                id = agendaItem.id,
                title = agendaItem.title,
                type = agendaItem.agendaItemOption.ordinal,
                description = agendaItem.description,
                date = agendaItem.date.toEpochMilli()
            ))
        }

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            agendaItem.alarmDate.toInstant().toEpochMilli(),
            PendingIntent.getBroadcast(
                context,
                agendaItem.id.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    override fun cancelAlarm(agendaItemId: String) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                agendaItemId.hashCode(),
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}