@file:OptIn(ExperimentalMaterial3Api::class)

package com.juandgaines.agenda.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.juandgaines.agenda.domain.utils.toEpochMilliUtc
import com.juandgaines.agenda.presentation.R
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemAction
import com.juandgaines.core.presentation.designsystem.TaskyBrown
import com.juandgaines.core.presentation.designsystem.TaskyDarkGreen
import com.juandgaines.core.presentation.designsystem.TaskyGreen
import com.juandgaines.core.presentation.designsystem.TaskyLight
import com.juandgaines.core.presentation.designsystem.TaskyLight2
import com.juandgaines.core.presentation.designsystem.TaskyOrange
import java.time.LocalDate

@Composable
fun AgendaTimePicker(
    modifier: Modifier = Modifier,
    onTimeSelected: (Int,Int)->Unit,
    onDismissDialog:()->Unit,
    initialHour:Int,
    initialMinutes:Int
) {
    val timePickerState = rememberTimePickerState(
        initialHour = initialHour,
        initialMinute = initialMinutes,
        is24Hour = true,
    )

    Column (
        modifier = modifier
            .background(
                TaskyLight, shape =
            MaterialTheme.shapes.medium
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        TimePicker(
            state = timePickerState,
            colors = TimePickerDefaults.colors(
                clockDialColor = TaskyDarkGreen,
                clockDialSelectedContentColor =  TaskyOrange,
                clockDialUnselectedContentColor = TaskyLight2,
                selectorColor = TaskyGreen,
                containerColor = TaskyLight,
                periodSelectorBorderColor = TaskyGreen,
                periodSelectorUnselectedContainerColor = TaskyBrown,
                periodSelectorSelectedContainerColor = TaskyGreen,
                periodSelectorSelectedContentColor = TaskyOrange,
                periodSelectorUnselectedContentColor = TaskyLight2,
                timeSelectorSelectedContainerColor = TaskyGreen,
                timeSelectorUnselectedContainerColor = TaskyBrown,
                timeSelectorSelectedContentColor = TaskyOrange,
                timeSelectorUnselectedContentColor = TaskyLight2
            )
        )
        Button(onClick = {
            onDismissDialog()
        }) {
            Text(
                "Dismiss picker",
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Button(
            onClick = {
                val selectedHour = timePickerState.hour
                val selectedMinute = timePickerState.minute

                onTimeSelected(selectedHour,selectedMinute)
            }) {
            Text("Confirm selection",color = MaterialTheme.colorScheme.onSurface)
        }
    }
}