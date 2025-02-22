@file:OptIn(ExperimentalMaterial3Api::class)

package com.juandgaines.agenda.presentation.home

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.juandgaines.agenda.domain.agenda.AgendaItems.Event
import com.juandgaines.agenda.domain.agenda.AgendaItems.Reminder
import com.juandgaines.agenda.domain.agenda.AgendaItems.Task
import com.juandgaines.agenda.presentation.R
import com.juandgaines.agenda.presentation.components.AgendaDatePicker
import com.juandgaines.agenda.presentation.home.AgendaActions.AgendaOperation
import com.juandgaines.agenda.presentation.home.AgendaActions.CreateItem
import com.juandgaines.agenda.presentation.home.AgendaActions.DismissCreateContextMenu
import com.juandgaines.agenda.presentation.home.AgendaActions.DismissDateDialog
import com.juandgaines.agenda.presentation.home.AgendaActions.DismissProfileMenu
import com.juandgaines.agenda.presentation.home.AgendaActions.Logout
import com.juandgaines.agenda.presentation.home.AgendaActions.SelectDate
import com.juandgaines.agenda.presentation.home.AgendaActions.SelectDateWithingRange
import com.juandgaines.agenda.presentation.home.AgendaActions.SendNotificationPermission
import com.juandgaines.agenda.presentation.home.AgendaActions.SendScheduleAlarmPermission
import com.juandgaines.agenda.presentation.home.AgendaActions.ShowCreateContextMenu
import com.juandgaines.agenda.presentation.home.AgendaActions.ShowDateDialog
import com.juandgaines.agenda.presentation.home.AgendaActions.ShowProfileMenu
import com.juandgaines.agenda.presentation.home.AgendaActions.ToggleDoneTask
import com.juandgaines.agenda.presentation.home.AgendaEvents.Error
import com.juandgaines.agenda.presentation.home.AgendaEvents.GoToItemScreen
import com.juandgaines.agenda.presentation.home.AgendaEvents.LogOut
import com.juandgaines.agenda.presentation.home.AgendaEvents.Success
import com.juandgaines.agenda.presentation.home.AgendaItemUi.Item
import com.juandgaines.agenda.presentation.home.AgendaItemUi.Needle
import com.juandgaines.agenda.presentation.home.componets.CurrentTimeDivider
import com.juandgaines.agenda.presentation.home.componets.ProfileIcon
import com.juandgaines.agenda.presentation.home.componets.agenda_cards.AgendaCard
import com.juandgaines.agenda.presentation.home.componets.selector_date.DateSelector
import com.juandgaines.agenda.presentation.utils.hasNotificationPermission
import com.juandgaines.agenda.presentation.utils.hasScheduleAlarmPermission
import com.juandgaines.agenda.presentation.utils.requestTaskyPermissions
import com.juandgaines.agenda.presentation.utils.shouldShowPostNotificationPermissionRationale
import com.juandgaines.core.domain.agenda.AgendaItemOption
import com.juandgaines.core.domain.agenda.AgendaItemOption.EVENT
import com.juandgaines.core.domain.agenda.AgendaItemOption.REMINDER
import com.juandgaines.core.domain.agenda.AgendaItemOption.TASK
import com.juandgaines.core.presentation.designsystem.AddIcon
import com.juandgaines.core.presentation.designsystem.ArrowDownIcon
import com.juandgaines.core.presentation.designsystem.TaskyTheme
import com.juandgaines.core.presentation.designsystem.components.TaskyActionButton
import com.juandgaines.core.presentation.designsystem.components.TaskyDialog
import com.juandgaines.core.presentation.designsystem.components.TaskyFAB
import com.juandgaines.core.presentation.designsystem.components.TaskyScaffold
import com.juandgaines.core.presentation.ui.ObserveAsEvents
import com.juandgaines.core.presentation.ui.UiText.StringResource

@Composable
fun AgendaScreenRoot(
    viewModel: AgendaViewModel,
    navigateToAgendaItem : (String?, AgendaItemOption,Boolean, Long?) -> Unit,
    navigateToLogin: () -> Unit
){
    val state by viewModel.state.collectAsStateWithLifecycle()
    val events = viewModel.events

    val context= LocalContext.current
    ObserveAsEvents(
        events
    ) { agendaEvents ->
        when (agendaEvents) {
            is LogOut -> {
                navigateToLogin()
                Toast.makeText(
                    context,
                     StringResource(R.string.logout_success).asString(context),
                    Toast.LENGTH_SHORT
                ).show()
            }
            is Error -> {
                Toast.makeText(
                    context,
                    agendaEvents.message.asString(context),
                    Toast.LENGTH_SHORT
                ).show()
            }
            is Success -> {
                Toast.makeText(
                    context,
                    agendaEvents.message.asString(context),
                    Toast.LENGTH_SHORT
                ).show()
            }

            is GoToItemScreen -> {
                navigateToAgendaItem(
                    agendaEvents.id,
                    agendaEvents.type,
                    agendaEvents.isEditing,
                    agendaEvents.dateEpochMilli
                )
            }
        }
    }

    AgendaScreen(
        stateAgenda = state,
        agendaActions =viewModel::onAction
    )
}

@Composable
fun AgendaScreen(
    stateAgenda: AgendaState,
    agendaActions: (AgendaActions)->Unit
) {
    val context = LocalContext.current
    val activity = LocalContext.current as ComponentActivity
    val alarmManager =
        activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    val launcherPermission = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { perms ->
        val hasNotificationPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
            && perms[android.Manifest.permission.POST_NOTIFICATIONS] != null
        ) {
            perms[android.Manifest.permission.POST_NOTIFICATIONS] == true
        } else {
            true
        }

        val showAskNotificationRationale = activity.shouldShowPostNotificationPermissionRationale()

        agendaActions(
            SendNotificationPermission(hasNotificationPermission, showAskNotificationRationale)
        )


    }

    LaunchedEffect(key1 = true) {
        val showNotificationRationale = activity.shouldShowPostNotificationPermissionRationale()

        agendaActions(
            SendNotificationPermission(
                activity.hasNotificationPermission(),
                showNotificationRationale
            )
        )
        val isGranted = activity.hasScheduleAlarmPermission()
        agendaActions(SendScheduleAlarmPermission(isGranted))

        if  (!showNotificationRationale) {
            launcherPermission.requestTaskyPermissions(activity)
        }
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
                        agendaActions(ShowDateDialog)
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
                            agendaActions(ShowProfileMenu)
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

            if (stateAgenda.isNotificationRationaleNeeded ||!stateAgenda.isScheduleAlarmPermissionAccepted) {
                TaskyDialog(
                    title = stringResource(id = R.string.permission_required),
                    onDismiss = {  },
                    description = when {
                        stateAgenda.isNotificationRationaleNeeded && !stateAgenda.isScheduleAlarmPermissionAccepted-> {
                            stringResource(id = R.string.alarm_and_notification_permission_rationale)
                        }

                        !stateAgenda.isScheduleAlarmPermissionAccepted -> {
                            stringResource(id = R.string.alarm_permission_rationale)
                        }

                        else -> {
                            stringResource(id = R.string.notification_permission_rationale)
                        }
                    },
                    primaryButton = {
                        TaskyActionButton(
                            text = stringResource(id = R.string.okay),
                            isLoading = false,
                            onClick = {

                                val canScheduleAlarmPermission = activity.hasScheduleAlarmPermission()

                                if (
                                    canScheduleAlarmPermission.not()
                                ){
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                        val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                                        intent.setData(
                                            Uri.fromParts("package", activity.packageName, null))
                                        startActivity(activity, intent, null)
                                    }
                                }
                                else {
                                    launcherPermission.launch(
                                        arrayOf(
                                            android.Manifest.permission.POST_NOTIFICATIONS,
                                        )
                                    )
                                }
                            },
                        )
                    }
                )
            }
        },
        floatingActionButton = {
            TaskyFAB(
                onClick = {
                    agendaActions(ShowCreateContextMenu)
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
                        agendaActions(SelectDateWithingRange(it))
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
                                    is Task-> {
                                        AgendaCard(
                                            onCheckClick = {
                                                agendaActions(ToggleDoneTask(item))
                                            },
                                            agendaItem = item,
                                            isDone = item.isDone,
                                            onClickItem = {
                                                agendaActions(
                                                    AgendaOperation(
                                                        AgendaCardMenuOperations.Open(item)
                                                    )
                                                )
                                            },
                                            title = item.title,
                                            description = item.description ?: "",
                                            onMenuItemClick = { operation ->
                                                agendaActions(AgendaOperation(operation))
                                            }
                                        )
                                    }
                                    is Reminder -> {
                                        AgendaCard(
                                            agendaItem = item,
                                            onClickItem = {
                                                agendaActions(
                                                    AgendaOperation(
                                                        AgendaCardMenuOperations.Open(item)
                                                    )
                                                )
                                            },
                                            title = item.title,
                                            description = item.description ?: "",
                                            onMenuItemClick = { operation ->
                                                agendaActions(AgendaOperation(operation))
                                            }
                                        )
                                    }
                                    is Event->{
                                        AgendaCard(
                                            agendaItem = item,
                                            onClickItem = {
                                                agendaActions(
                                                    AgendaOperation(
                                                        AgendaCardMenuOperations.Open(item)
                                                    )
                                                )
                                            },
                                            title = item.title,
                                            description = item.description ?: "",
                                            onMenuItemClick = { operation ->
                                                agendaActions(AgendaOperation(operation))
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
                    onDateSelected = { date->
                        agendaActions(SelectDate(date))
                    },
                    onDismissDialog = {
                        agendaActions(DismissDateDialog)
                    },
                    initialDate = stateAgenda.selectedLocalDate
                )
            }
            Box(
                modifier = Modifier.align(Alignment.BottomEnd)
            ){
                DropdownMenu(
                    expanded = stateAgenda.isCreateContextMenuVisible,
                    onDismissRequest = { agendaActions(DismissCreateContextMenu) },
                ) {

                    AgendaItemOption.entries.forEach { agendaItem ->
                        when (agendaItem) {
                            REMINDER -> {
                                DropdownMenuItem(
                                    onClick = {
                                        agendaActions(CreateItem(agendaItem))
                                    },
                                    text = { Text(stringResource(R.string.reminder)) }
                                )
                            }

                            TASK -> {
                                DropdownMenuItem(
                                    onClick = {
                                        agendaActions(CreateItem(agendaItem))
                                    },
                                    text = { Text(stringResource(R.string.task)) }
                                )
                            }

                            EVENT -> {
                                DropdownMenuItem(
                                    onClick = {
                                        agendaActions(CreateItem(agendaItem))
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
                    onDismissRequest = { agendaActions(DismissProfileMenu) },
                ) {
                    DropdownMenuItem(
                        onClick = {
                            agendaActions(Logout)
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