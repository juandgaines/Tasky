@file:OptIn(ExperimentalMaterial3Api::class)

package com.juandgaines.agenda.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.juandgaines.agenda.componets.ProfileIcon
import com.juandgaines.agenda.componets.selector_date.DateSelector
import com.juandgaines.core.presentation.designsystem.AddIcon
import com.juandgaines.core.presentation.designsystem.ArrowDownIcon
import com.juandgaines.core.presentation.designsystem.TaskyTheme
import com.juandgaines.core.presentation.designsystem.components.TaskyFAB
import com.juandgaines.core.presentation.designsystem.components.TaskyScaffold

@Composable
fun AgendaScreenRoot(
    viewModel: AgendaViewModel
){
    val state = viewModel.state

    AgendaScreen(
        stateAgenda = state,
        agendaActions = viewModel::onAction
    )
}

@Composable
fun AgendaScreen(
    stateAgenda: AgendaState,
    agendaActions: (AgendaActions)->Unit
) {
    TaskyScaffold (
        fabPosition = FabPosition.End,
        topAppBar = {
            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .height(56.dp)
            ){
                Text(
                    text = stateAgenda.currentMonth,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                         agendaActions(AgendaActions.ShowDateDialog)
                    }
                )
                Icon(
                    imageVector = ArrowDownIcon,
                    contentDescription = null,
                )
                Spacer(modifier = Modifier.weight(1f))
                ProfileIcon(
                    initials = "JD",
                    modifier = Modifier.size(36.dp)
                )
            }
        },
        floatingActionButton = {
            TaskyFAB(
                icon = AddIcon,
                onClick = {

                }
            )
        }
    ){
        DateSelector(
            modifier = Modifier.fillMaxWidth(),
            daysList = stateAgenda.dateRange,
            onSelectDate = {
                agendaActions(AgendaActions.SelectDateWithingRange(it))
            }
        )

        if (stateAgenda.isDatePickerOpened) {
            val datePickerState = rememberDatePickerState()
            DatePickerDialog(
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
                        Text("Select")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            agendaActions(AgendaActions.DismissDateDialog)
                        }
                    ) {
                        Text("Cancel")
                    }
                },
            ){
                DatePicker(
                    state = datePickerState
                )
            }
        }
    }
}


@Composable
@Preview
fun AgendaScreenPreview() {
    TaskyTheme {
        AgendaScreen(
            stateAgenda = AgendaState(),
            agendaActions = {}
        )
    }
}