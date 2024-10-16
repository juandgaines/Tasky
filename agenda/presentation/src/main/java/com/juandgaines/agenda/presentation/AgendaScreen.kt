@file:OptIn(ExperimentalMaterial3Api::class)

package com.juandgaines.agenda.presentation

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.juandgaines.agenda.componets.AgendaDatePicker
import com.juandgaines.agenda.componets.CurrentTimeDivider
import com.juandgaines.agenda.componets.ProfileIcon
import com.juandgaines.agenda.componets.agenda_cards.AgendaCard
import com.juandgaines.agenda.componets.selector_date.DateSelector
import com.juandgaines.agenda.domain.reminder.Reminder
import com.juandgaines.agenda.domain.task.Task
import com.juandgaines.agenda.domain.utils.toFormattedSingleDateTime
import com.juandgaines.agenda.presentation.AgendaItemOption.EVENT
import com.juandgaines.agenda.presentation.AgendaItemOption.REMINDER
import com.juandgaines.agenda.presentation.AgendaItemOption.TASK
import com.juandgaines.agenda.presentation.AgendaItemUi.Item
import com.juandgaines.agenda.presentation.AgendaItemUi.Needle
import com.juandgaines.core.presentation.designsystem.AddIcon
import com.juandgaines.core.presentation.designsystem.ArrowDownIcon
import com.juandgaines.core.presentation.designsystem.TaskyTheme
import com.juandgaines.core.presentation.designsystem.components.TaskyFAB
import com.juandgaines.core.presentation.designsystem.components.TaskyScaffold
import com.juandgaines.core.presentation.ui.ObserveAsEvents
import com.juandgaines.core.presentation.ui.UiText

@Composable
fun AgendaScreenRoot(
    viewModel: AgendaViewModel,
    navigateToLogin: () -> Unit
){
    val state = viewModel.state
    val events = viewModel.events

    val context= LocalContext.current
    ObserveAsEvents(
        events
    ) { agendaEvents ->
        when (agendaEvents) {
            is AgendaEvents.LogOut -> {
                navigateToLogin()
                Toast.makeText(
                    context,
                     UiText.StringResource(R.string.logout_success).asString(context),
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> Unit
        }

    }

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
    val density = LocalDensity.current


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
                    initials = stateAgenda.userInitials,
                    modifier = Modifier
                        .size(36.dp)
                        .clickable {
                            agendaActions(AgendaActions.ShowProfileMenu)
                        }
                )
            }
        },
        loading = {
            if (stateAgenda.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                )
            }
        },
        floatingActionButton = {
            TaskyFAB(
                onClick = {
                    agendaActions(AgendaActions.ShowCreateContextMenu)
                },
                icon = AddIcon,
            )
        }
    ){
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column (
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ){
                DateSelector(
                    modifier = Modifier.fillMaxWidth(),
                    daysList = stateAgenda.dateRange,
                    onSelectDate = {
                        agendaActions(AgendaActions.SelectDateWithingRange(it))
                    }
                )
                Text(
                    text = stateAgenda.labelDateRange,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                LazyColumn (
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ){
                    items(stateAgenda.agendaItems) { agendaItem ->

                        when (agendaItem) {
                            is Item -> {
                                when (val item= agendaItem.agendaItem) {
                                    is Task -> {
                                        AgendaCard(
                                            onCheckClick = {
                                            },
                                            agendaItem = item,
                                            isDone = item.isDone,
                                            onClickItem = {

                                            },
                                            title = item.title,
                                            description = item.description ?: "",
                                            date = item.time.toFormattedSingleDateTime(),
                                            onMenuItemClick = { operation ->
                                                agendaActions(AgendaActions.AgendaOperation(operation))
                                            }
                                        )
                                    }
                                    is Reminder -> {
                                        AgendaCard(
                                            agendaItem = item,
                                            onClickItem = {

                                            },
                                            title = item.title,
                                            description = item.description ?: "",
                                            date = item.time.toFormattedSingleDateTime(),
                                            onMenuItemClick = {

                                            }
                                        )
                                    }
                                }
                            }
                            is Needle -> {
                                CurrentTimeDivider()
                            }
                        }

                    }
                }
            }

            if (stateAgenda.isDatePickerOpened) {
                AgendaDatePicker(
                    agendaActions = agendaActions,
                    initialDate = stateAgenda.selectedLocalDate
                )
            }
            Box(
                modifier = Modifier.align(Alignment.BottomEnd)
            ){
                DropdownMenu(
                    expanded = stateAgenda.isCreateContextMenuVisible,
                    onDismissRequest = { agendaActions(AgendaActions.DismissCreateContextMenu) },
                ) {

                    AgendaItemOption.entries.forEach { agendaItem ->
                        when (agendaItem) {
                            REMINDER -> {
                                DropdownMenuItem(
                                    onClick = {
                                        agendaActions(AgendaActions.CreateItem(agendaItem))
                                    },
                                    text = { Text(stringResource(R.string.reminder)) }
                                )
                            }

                            TASK -> {
                                DropdownMenuItem(
                                    onClick = {
                                        agendaActions(AgendaActions.CreateItem(agendaItem))
                                    },
                                    text = { Text(stringResource(R.string.task)) }
                                )
                            }

                            EVENT -> {
                                DropdownMenuItem(
                                    onClick = {
                                        agendaActions(AgendaActions.CreateItem(agendaItem))
                                    },
                                    text = { Text(stringResource(R.string.event)) }
                                )
                            }
                        }
                    }
                }
            }

            Box(modifier = Modifier.align(Alignment.TopEnd)){
                DropdownMenu(
                    expanded = stateAgenda.isProfileMenuVisible,
                    onDismissRequest = { agendaActions(AgendaActions.DismissProfileMenu) },
                ) {
                    DropdownMenuItem(
                        onClick = {
                            agendaActions(AgendaActions.Logout)
                        },
                        text = { Text(stringResource(R.string.logout)) }
                    )
                }
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