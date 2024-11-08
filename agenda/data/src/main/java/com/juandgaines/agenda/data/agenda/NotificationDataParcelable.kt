package com.juandgaines.agenda.data.agenda

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NotificationDataParcelable(
    val id: String,
    val title: String,
    val type: Int,
    val description: String?,
    val date: Long
) : Parcelable