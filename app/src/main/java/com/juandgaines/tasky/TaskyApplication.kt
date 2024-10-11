package com.juandgaines.tasky

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

@HiltAndroidApp
class TaskyApplication:Application(){

    val applicationScope = CoroutineScope(SupervisorJob())
}