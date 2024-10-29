@file:OptIn(ExperimentalMaterial3Api::class)

package com.juandgaines.agenda.presentation.home.componets

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
import com.juandgaines.agenda.domain.utils.toEpochMilliUtc
import com.juandgaines.agenda.presentation.home.AgendaActions
import com.juandgaines.agenda.presentation.R
import java.time.LocalDate

@Composable
fun AgendaDatePicker(
    modifier: Modifier = Modifier,
    onDateSelected: (Long)->Unit,
    onDismissDialog:()->Unit,
    initialDate:LocalDate
) {
    val datePickerState = rememberDatePickerState(
        initialDate.toEpochMilliUtc()
    )
    DatePickerDialog(
        modifier = modifier,
        onDismissRequest = {
            onDismissDialog()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let { date->
                        onDateSelected(date)
                    }
                }
            ){
                Text(stringResource(R.string.select))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissDialog()
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