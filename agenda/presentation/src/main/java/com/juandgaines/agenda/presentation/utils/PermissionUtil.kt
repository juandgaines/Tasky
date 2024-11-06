package com.juandgaines.agenda.presentation.utils

import android.Manifest
import android.app.AlarmManager
import android.content.Context
import android.os.Build
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService

fun ComponentActivity.shouldShowExactAlarmPermissionRationale(): Boolean {
    return if (VERSION.SDK_INT >= VERSION_CODES.S) {
        val alarmManager: AlarmManager = this.getSystemService<AlarmManager>()!!
        alarmManager.canScheduleExactAlarms().not()
    } else false
}

fun ComponentActivity.shouldShowPostNotificationPermissionRationale(): Boolean {
    return VERSION.SDK_INT >= VERSION_CODES.TIRAMISU &&
        shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)
}

private fun Context.hasPermission(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        permission
    ) == android.content.pm.PackageManager.PERMISSION_GRANTED
}

fun Context.hasNotificationPermission(): Boolean {
    return if (VERSION.SDK_INT >= VERSION_CODES.TIRAMISU) {
        hasPermission(android.Manifest.permission.POST_NOTIFICATIONS)
    } else true
}

fun Context.hasScheduleAlarmPermission(): Boolean {
    return if (VERSION.SDK_INT >= VERSION_CODES.S) {
        val alarmManager: AlarmManager = this.getSystemService<AlarmManager>()!!
        alarmManager.canScheduleExactAlarms()
    } else {
        true
    }
}

fun ActivityResultLauncher<Array<String>>.requestTaskyPermissions(
    context: Context,
) {

    val hasNotificationPermission = context.hasNotificationPermission()

    val permissionsToBeAsked = mutableListOf<String>()


    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !hasNotificationPermission) {
        permissionsToBeAsked.add(Manifest.permission.POST_NOTIFICATIONS)
    }

    if (permissionsToBeAsked.isEmpty()) {
        return
    }
    launch(permissionsToBeAsked.toTypedArray())
}

