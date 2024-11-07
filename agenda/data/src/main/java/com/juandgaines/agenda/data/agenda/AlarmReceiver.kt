package com.juandgaines.agenda.data.agenda

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.content.getSystemService
import androidx.core.net.toUri
import com.juandgaines.agenda.data.R
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class AlarmReceiver : BroadcastReceiver() {



    override fun onReceive(
        context: Context?,
        intent: Intent?,
    ) {
        val notificationManager =
            context?.getSystemService<NotificationManager>()!!

        val baseNotification = NotificationCompat.Builder(context!!, CHANNEL_ID)
                .setSmallIcon(R.drawable.tasky_logo)
                .setContentTitle(context.getString(R.string.agenda_item))

        createNotificationChannel(context, notificationManager)
        Log.d("AlarmReceiver", "Alarm received")
        val activityIntent = Intent("com.tasky.ALARM_RING").apply {
            data = "tasky://agenda_item".toUri()
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        val pendingIntent = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(activityIntent)
            getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
        }
        val notification = baseNotification
            .setContentText("Alarmeichon")
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
    }
}