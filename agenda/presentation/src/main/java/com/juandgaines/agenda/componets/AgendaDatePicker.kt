@file:OptIn(ExperimentalMaterial3Api::class)

package com.juandgaines.agenda.componets

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.juandgaines.agenda.domain.utils.toUtcZonedDateTime
import com.juandgaines.agenda.presentation.AgendaActions
import com.juandgaines.agenda.presentation.R
import java.time.ZonedDateTime

@Composable
fun AgendaDatePicker(
    modifier: Modifier = Modifier,
    agendaActions: (AgendaActions)->Unit,
    initialDate:ZonedDateTime
) {
    val datePickerState = rememberDatePickerState(
        initialDate.toUtcZonedDateTime().toEpochSecond().times(1000)
    )
    DatePickerDialog(
        modifier = modifier,
        onDismissRequest = {
            agendaActions(AgendaActions.DismissDateDialog)
        },
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let { date->
                        agendaActions(AgendaActions.SelectDate(date))
                    }
                }
            ){
                Text(stringResource(R.string.select))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    agendaActions(AgendaActions.DismissDateDialog)
                }
            ) {
                Text(stringResource(R.string.cancel))
            }
        },
    ){
        DatePicker(
            colors = DatePickerDefaults.colors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,

            ),
            state = datePickerState
        )
    }
}