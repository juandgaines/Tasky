package com.juandgaines.agenda.data.agenda

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.content.getSystemService
import androidx.core.net.toUri
import com.juandgaines.agenda.data.R
import com.juandgaines.agenda.domain.utils.toFormattedTime
import com.juandgaines.agenda.domain.utils.toZonedDateTime

class AlarmReceiver : BroadcastReceiver() {



    override fun onReceive(
        context: Context?,
        intent: Intent?,
    ) {

        val title = intent?.getStringExtra(TITLE)
        val description = intent?.getStringExtra(DESCRIPTION)
        val agendaItemId = intent?.getStringExtra(AGENDA_ITEM_ID)
        val time = intent?.getLongExtra(TIME, 0)?.toZonedDateTime()?.toFormattedTime()

        val notificationManager =
            context?.getSystemService<NotificationManager>()!!

        val baseNotification = NotificationCompat.Builder(context!!, CHANNEL_ID)
                .setSmallIcon(R.drawable.tasky_logo)
                .setContentTitle("Alarm: $title at $time")

        createNotificationChannel(context, notificationManager)
        val activityIntent = Intent("com.tasky.ALARM_RING").apply {
            data = "tasky://agenda_item".toUri()
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        val pendingIntent = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(activityIntent)
            getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
        }
        val notification = baseNotification
            .setContentText(description)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(1, notification)


    }

    private fun createNotificationChannel(applicationContext: Context, notificationManager: NotificationManager) {
        val channel = NotificationChannel(
            CHANNEL_ID,
            applicationContext.getString(R.string.agenda_item),
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        const val CHANNEL_ID = "alarms"
        const val TITLE = "title"
        const val DESCRIPTION = "message"
        const val AGENDA_ITEM_ID = "alarm_id"
        const val TIME = "time"
    }
}