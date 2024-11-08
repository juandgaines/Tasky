package com.juandgaines.agenda.data.agenda

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.juandgaines.agenda.data.R
import com.juandgaines.agenda.domain.agenda.AgendaItems.Event.description
import com.juandgaines.agenda.domain.utils.toFormattedTime
import com.juandgaines.agenda.domain.utils.toZonedDateTime

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(
        context: Context?,
        intent: Intent?,
    ) {
        val notificationData = if (Build.VERSION.SDK_INT >= 33) {
            intent?.getParcelableExtra(NOTIFICATION_DATA, NotificationDataParcelable::class.java)
        } else {
            intent?.getParcelableExtra<NotificationDataParcelable>(NOTIFICATION_DATA)
        }

        val notificationManager = ContextCompat.getSystemService(
            context!!,
            NotificationManager::class.java
        ) as NotificationManager

        val baseNotification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.tasky_logo)
                .setContentTitle("Alarm: ${notificationData?.title} at ${notificationData?.date?.toZonedDateTime()?.toFormattedTime()}")

        createNotificationChannel(context, notificationManager)

        val activityIntent = Intent(Intent.ACTION_VIEW).apply {
            data = "tasky://agenda_item/${notificationData?.type}?id=${notificationData?.id}&isEditing=${0}&dateEpochMillis=${notificationData?.date}".toUri()
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }


        val pendingIntent = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(activityIntent)
            getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
        }
        val notification = baseNotification
            .setContentText(description)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
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
        const val TYPE = "type"
        const val DESCRIPTION = "message"
        const val AGENDA_ITEM_ID = "alarm_id"
        const val TIME = "time"
        const val NOTIFICATION_DATA = "notification_data"
    }
}