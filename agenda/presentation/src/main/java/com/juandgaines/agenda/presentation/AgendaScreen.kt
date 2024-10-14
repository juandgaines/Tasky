@file:OptIn(ExperimentalMaterial3Api::class)

package com.juandgaines.agenda.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.juandgaines.agenda.componets.AgendaDatePicker
import com.juandgaines.agenda.componets.ProfileIcon
import com.juandgaines.agenda.componets.selector_date.DateSelector
import com.juandgaines.agenda.domain.agenda.AgendaItems.Reminder
import com.juandgaines.agenda.domain.agenda.AgendaItems.Task
import com.juandgaines.agenda.domain.agenda.agendaItems
import com.juandgaines.core.presentation.designsystem.AddIcon
import com.juandgaines.core.presentation.designsystem.ArrowDownIcon
import com.juandgaines.core.presentation.designsystem.TaskyTheme
import com.juandgaines.core.presentation.designsystem.components.TaskyFAB
import com.juandgaines.core.presentation.designsystem.components.TaskyScaffold
import com.juandgaines.core.presentation.ui.ObserveAsEvents

@Composable
fun AgendaScreenRoot(
    viewModel: AgendaViewModel,
    navigateToLogin: () -> Unit
){
    val state = viewModel.state
    val events = viewModel.events

    ObserveAsEvents(
        events
    ) { agendaEvents ->
        when (agendaEvents) {
            is AgendaEvents.LogOut -> {
                navigateToLogin()
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

    var pressOffSet by remember {
        mutableStateOf(DpOffset.Zero)
    }
    var pressOffSetProfile by remember {
        mutableStateOf(DpOffset.Zero)
    }
    val density = LocalDensity.current
    var itemHeight by remember {
        mutableStateOf(0.dp)
    }

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
                        .onGloballyPositioned { coordinates->
                            val y = with(density) { coordinates.positionInParent().y.toDp()}
                            val x = with(density) { coordinates.positionInParent().x.toDp()}
                            itemHeight = with(density) { coordinates.size.height.toDp() }
                            pressOffSetProfile = DpOffset(
                                x,
                                y
                            )
                        }
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
                modifier = Modifier
                    .onGloballyPositioned { coordinates->
                        pressOffSet = DpOffset(
                            with(density) { coordinates.positionInParent().x.toDp()},
                            with(density) { coordinates.positionInParent().y.toDp()},
                        )
                    },
                onClick = {
                    agendaActions(AgendaActions.ShowCreateContextMenu)
                },
                icon = AddIcon,
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

        Text(
            text = stateAgenda.labelDateRange,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSecondary,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        if (stateAgenda.isDatePickerOpened) {
            AgendaDatePicker(
                agendaActions = agendaActions,
                initialDate = stateAgenda.selectedLocalDate
            )
        }

        DropdownMenu(
            expanded = stateAgenda.isCreateContextMenuVisible,
            onDismissRequest = { agendaActions(AgendaActions.DismissCreateContextMenu) },
            offset = pressOffSet.copy(
                y = pressOffSet.y - itemHeight,
                x = pressOffSet.x
            )
        ) {

            agendaItems.forEach { agendaItem ->
                when (agendaItem) {
                    Reminder -> {
                        DropdownMenuItem(
                            onClick = {
                                agendaActions(AgendaActions.CreateAgendaItem(agendaItem))
                            },
                            text = { Text(stringResource(R.string.reminder)) }
                        )
                    }

                    Task -> {
                        DropdownMenuItem(
                            onClick = {
                                agendaActions(AgendaActions.CreateAgendaItem(agendaItem))
                            },
                            text = { Text(stringResource(R.string.task)) }
                        )
                    }

                }
            }
        }

        DropdownMenu(
            expanded = stateAgenda.isProfileMenuVisible,
            onDismissRequest = { agendaActions(AgendaActions.DismissProfileMenu) },
            offset = pressOffSetProfile.copy(
                y = pressOffSetProfile.y + itemHeight,
                x = pressOffSetProfile.x
            )
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