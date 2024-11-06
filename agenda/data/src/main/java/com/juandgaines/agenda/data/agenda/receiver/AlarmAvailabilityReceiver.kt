package com.juandgaines.agenda.data.agenda.receiver

import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import com.juandgaines.agenda.domain.agenda.AlarmProvider
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@AndroidEntryPoint
class AlarmAvailabilityReceiver @Inject constructor(
    @ApplicationContext
    private val context: Context,
): BroadcastReceiver(), AlarmProvider {

    private val _alarmAvailable = MutableStateFlow<Boolean>(false)

    override fun onReceive(
        context: Context?,
        intent: Intent?,
    ) {
        if (intent?.action == AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED) {
            context?.let {
                val alarmManager =
                    context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

                val isGranted = if (VERSION.SDK_INT >= VERSION_CODES.S) {
                    alarmManager.canScheduleExactAlarms()
                } else {
                    true
                }
                _alarmAvailable.value = isGranted

            }
        }
    }

    override fun register() {
        if (VERSION.SDK_INT >= VERSION_CODES.S) {

            val filter =
                IntentFilter(AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED)
            ContextCompat.registerReceiver(
                context,
                this,
                filter,
                ContextCompat.RECEIVER_EXPORTED
            )
        }
    }

    override fun alarmAvailable(): StateFlow<Boolean> = _alarmAvailable.asStateFlow()

    override fun unregister() {
        if (VERSION.SDK_INT >= VERSION_CODES.S) {
            context.unregisterReceiver(this)
        }
    }
}

