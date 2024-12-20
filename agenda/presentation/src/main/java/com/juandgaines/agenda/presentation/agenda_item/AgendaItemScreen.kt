@file:OptIn(ExperimentalMaterial3Api::class)

package com.juandgaines.agenda.presentation.agenda_item

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.juandgaines.agenda.domain.agenda.AgendaItems
import com.juandgaines.agenda.presentation.R
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemAction.DismissDateDialog
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemAction.SelectDateStart
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemDetailsUi.EventDetails
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemEvent.Created
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemEvent.CreationScheduled
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemEvent.Deleted
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemEvent.DeletionScheduled
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemEvent.Error
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemEvent.Joined
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemEvent.Left
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemEvent.UpdateScheduled
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemEvent.Updated
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemEvent.UserAdded
import com.juandgaines.agenda.presentation.agenda_item.components.AgendaItemTypeSection
import com.juandgaines.agenda.presentation.agenda_item.components.AlarmSection
import com.juandgaines.agenda.presentation.agenda_item.components.DateSection
import com.juandgaines.agenda.presentation.agenda_item.components.DescriptionSection
import com.juandgaines.agenda.presentation.agenda_item.components.PhotosSection
import com.juandgaines.agenda.presentation.agenda_item.components.TitleSection
import com.juandgaines.agenda.presentation.agenda_item.components.attendee.AttendeeSection
import com.juandgaines.agenda.presentation.components.AgendaDatePicker
import com.juandgaines.agenda.presentation.components.AgendaTimePicker
import com.juandgaines.core.domain.auth.PatternValidator
import com.juandgaines.core.presentation.designsystem.CloseIcon
import com.juandgaines.core.presentation.designsystem.EditIcon
import com.juandgaines.core.presentation.designsystem.TaskyGray
import com.juandgaines.core.presentation.designsystem.TaskyLight
import com.juandgaines.core.presentation.designsystem.TaskyTheme
import com.juandgaines.core.presentation.designsystem.components.TaskyActionButton
import com.juandgaines.core.presentation.designsystem.components.TaskyEditTextDialog
import com.juandgaines.core.presentation.designsystem.components.TaskyScaffold
import com.juandgaines.core.presentation.ui.ObserveAsEvents
import java.time.ZonedDateTime

@Composable
fun AgendaItemScreenRoot(
    viewModel: AgendaItemViewModel,
    navigateBack: () -> Unit,
    navigateEditField: (String, String) -> Unit,
    title: String?,
    description: String?,
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val events = viewModel.events

    ObserveAsEvents(events) { agendaItemEvents->
        when (agendaItemEvents){
            Created -> {
                Toast.makeText(
                    context,
                    context.getString(R.string.item_created),
                    Toast.LENGTH_SHORT
                ).show()
                navigateBack()
            }

            is Error -> {
                Toast.makeText(
                    context,
                    agendaItemEvents.uiText.asString(context),
                    Toast.LENGTH_SHORT
                ).show()
            }
            Updated -> {
                Toast.makeText(
                    context,
                    context.getString(R.string.item_updated),
                    Toast.LENGTH_SHORT
                ).show()
                navigateBack()
            }

            CreationScheduled -> {
                Toast.makeText(
                    context,
                    context.getString(R.string.item_created_scheduled),
                    Toast.LENGTH_SHORT
                ).show()
                navigateBack()
            }
            Deleted -> {
                Toast.makeText(
                    context,
                    context.getString(R.string.item_deleted),
                    Toast.LENGTH_SHORT
                ).show()
                navigateBack()
            }
            DeletionScheduled -> {
                Toast.makeText(
                    context,
                    context.getString(R.string.item_deleted_scheduled),
                    Toast.LENGTH_SHORT
                ).show()
                navigateBack()
            }
            UpdateScheduled -> {
                Toast.makeText(
                    context,
                    context.getString(R.string.item_updated_scheduled),
                    Toast.LENGTH_SHORT
                ).show()
                navigateBack()
            }

            is UserAdded -> {
                Toast.makeText(
                    context,
                    context.getString(R.string.visitor_added, agendaItemEvents.email.asString(context)),
                    Toast.LENGTH_SHORT
                ).show()
            }

            Left -> {
                Toast.makeText(
                    context,
                    context.getString(R.string.item_left),
                    Toast.LENGTH_SHORT
                ).show()
                navigateBack()
            }
            Joined -> {
                Toast.makeText(
                    context,
                    context.getString(R.string.item_joined),
                    Toast.LENGTH_SHORT
                ).show()
                navigateBack()
            }
        }
    }

    LaunchedEffect(title , description) {
        title?.let {
            viewModel.onAction(
                AgendaItemAction.UpdateField(
                    AgendaItems.EDIT_FIELD_TITLE_KEY,title
                )
            )
        }
        description?.let {
            viewModel.onAction(
                AgendaItemAction.UpdateField(
                    AgendaItems.EDIT_FIELD_TITLE_DESCRIPTION,description
                )
            )
        }
    }

    AgendaItemScreen(
        state = state,
        onAction = {  action->
            when(action){
                is AgendaItemAction.EditField -> {
                    navigateEditField(action.key, action.value)
                }
                is AgendaItemAction.Close -> {
                    navigateBack()
                }
                else -> viewModel.onAction(action)
            }

        }
    )

}

@Composable
fun AgendaItemScreen(
    state: AgendaItemState,
    onAction: (AgendaItemAction) -> Unit
) {
    val agendaItemName = when (state.details) {
        is AgendaItemDetailsUi.ReminderDetails -> stringResource(id = R.string.reminder)
        is AgendaItemDetailsUi.EventDetails -> stringResource(id = R.string.event)
        is AgendaItemDetailsUi.TaskDetails -> stringResource(id = R.string.task)
    }

    val canEditField = when (state.details) {
        is AgendaItemDetailsUi.EventDetails -> state.details.isUserCreator || state.isNew
        else -> true
    }

    TaskyScaffold (
        topAppBar = {
            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .height(56.dp)
            ){
                Icon(
                    imageVector = CloseIcon,
                    contentDescription = "Close",
                    modifier = Modifier.clickable {
                        onAction(AgendaItemAction.Close)
                    }
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = state.title,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    contentAlignment = Alignment.Center,
                ){
                    if (state.isEditing) {
                        Text(
                            text = stringResource(R.string.save),
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier.clickable {
                                onAction(AgendaItemAction.Save)
                            }
                        )
                    }
                    else {
                        Icon(
                            imageVector = EditIcon,
                            contentDescription = "Edit",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.clickable {
                                onAction(AgendaItemAction.Edit)
                            }
                        )
                    }
                }
            }
        },
        loading = {
            if(state.isSelectDateDialog){
                AgendaDatePicker(
                    onDateSelected = { date->
                        when(state.details) {
                            is AgendaItemDetailsUi.EventDetails ->{
                                if (state.isEditingEndDate) {
                                    onAction(AgendaItemAction.SelectDateFinish(date))
                                }
                                else {
                                    onAction(SelectDateStart(date))
                                }
                            }
                            else -> onAction(SelectDateStart(date))
                        }
                    },
                    onDismissDialog = {
                        onAction(DismissDateDialog)
                    },
                    initialDate = state.startDateTime.toLocalDate()
                )
            }

            if (state.isSelectTimeDialogVisible) {
                val hour = state.startDateTime.hour
                val minute = state.startDateTime.minute
                AgendaTimePicker(
                    modifier = Modifier.align(
                        Alignment.Center
                    ),
                    onTimeSelected = { h, m ->
                        when(state.details) {
                            is AgendaItemDetailsUi.EventDetails ->{
                                if (state.isEditingEndDate) {
                                    onAction(AgendaItemAction.SelectTimeFinish(h, m))
                                }
                                else {
                                    onAction(AgendaItemAction.SelectTimeStart(h, m))
                                }
                            }
                            else -> onAction(AgendaItemAction.SelectTimeStart(h, m))
                        }
                    },
                    onDismissDialog = {
                        onAction(AgendaItemAction.DismissTimeDialog)
                    },
                    initialHour = hour,
                    initialMinutes = minute
                )

            }


            if (state.details is AgendaItemDetailsUi.EventDetails && state.details.isAddAttendeeDialogVisible) {
                val isEmailValid by  state.details.isEmailValid.collectAsState(false)

                TaskyEditTextDialog(
                    title = stringResource(id = R.string.add_visitor),
                    onDismiss = {
                        onAction(AgendaItemAction.DismissAttendeeDialog)
                    },
                    textState = state.details.attendeeEmailBuffer,
                    isError = state.details.doesEmailExist,
                    primaryButton = {
                        TaskyActionButton(
                            text = stringResource(id = R.string.add),
                            isLoading = state.details.isAddingVisitor,
                            enabled =  isEmailValid && !
                            state.details.isAddingVisitor,
                            onClick = {
                                onAction(
                                    AgendaItemAction.AddEmailAsAttendee(
                                        state.details.attendeeEmailBuffer.text.toString()
                                    )
                                )
                            },
                        )
                    }
                )
            }
        }
    ){
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ){
            AgendaItemTypeSection(
                agendaItemDetails = state.details,
                agendaItemName = agendaItemName
            )
            TitleSection(
                title = state.title,
                isEditing = state.isEditing,
                canEditField = canEditField,
                onEditTitle = {
                    onAction(AgendaItemAction.EditField(AgendaItems.EDIT_FIELD_TITLE_KEY, state.title))
                }
            )
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth(),
                color = TaskyLight
            )

            DescriptionSection(
                description = state.description,
                isEditing = state.isEditing,
                canEditField = canEditField,
                onEditDescription = {
                    onAction(AgendaItemAction.EditField(AgendaItems.EDIT_FIELD_TITLE_DESCRIPTION, state.description))
                }
            )
            if (state.details is AgendaItemDetailsUi.EventDetails) {
                PhotosSection(
                    isEditing = state.isEditing,
                    canEditField = canEditField,
                    photos = state.details.photos,
                    local = state.details.localPhotos,
                    isInternetConnected = state.details.isConnectedToInternet,
                    onAddPhoto = { uri ->
                        onAction(AgendaItemAction.AddPicture(uri))
                    }
                )
            }

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth(),
                color = TaskyLight
            )

            DateSection(
                date = state.startDateTime,
                isEditing = state.isEditing,
                canEditField = canEditField,
                onEditStartDate = {
                    onAction(AgendaItemAction.ShowDateDialog())
                },
                onEditStartTime = {
                    onAction(AgendaItemAction.ShowTimeDialog())
                },
                title =stringResource(id = R.string.from)
            )
            if (state.details is AgendaItemDetailsUi.EventDetails) {
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth(),
                    color = TaskyLight
                )
                DateSection(
                    date = state.details.finishDate,
                    title = stringResource(id = R.string.to),
                    isEditing = state.isEditing,
                    onEditStartDate = {
                        onAction(AgendaItemAction.ShowDateDialog(true))
                    },
                    onEditStartTime = {
                        onAction(AgendaItemAction.ShowTimeDialog(true))
                    },
                    canEditField = canEditField
                )
            }

            AlarmSection(
                alarm = state.alarm,
                isEditing = state.isEditing,
                onSelectAlarmTime = { alarm->
                    onAction(AgendaItemAction.SelectAlarm(alarm))
                }
            )
            if (state.details is AgendaItemDetailsUi.EventDetails) {

                    AttendeeSection(
                        modifier = Modifier
                            .weight(1f),
                        selectedFilter = state.attendeeFilter,
                        attendeesGoing = state.details.isGoing,
                        attendeesNotGoing = state.details.isNotGoing,
                        isEditing = state.isEditing,
                        canEditField = canEditField,
                        isInternetConnected = state.details.isConnectedToInternet,
                        onSelectFilter = { filter ->
                            onAction(AgendaItemAction.SelectAttendeeFilter(filter))
                        },
                        isOwner = state.details.isUserCreator,
                        isCreating = state.isNew,
                        onRemoveAttendee = { attendeeId ->
                            onAction(AgendaItemAction.RemoveAttendee(attendeeId))
                        },
                        onAddAttendee = {
                            onAction(AgendaItemAction.ShowAttendeeDialog)
                        },
                    )
                }

            if(!state.isNew){
                when (state.details){
                    is EventDetails -> {

                        val text = when{
                            state.details.isUserCreator->{
                                stringResource(id = R.string.delete_item, agendaItemName).uppercase()
                            }
                            state.details.isGoingUser -> {
                                stringResource(id = R.string.leave_item, agendaItemName).uppercase()
                            }
                            else -> {
                                stringResource(id = R.string.join_item, agendaItemName).uppercase()
                            }
                        }

                        Text(
                            text = text,
                            style = MaterialTheme.typography.titleSmall,
                            color = TaskyGray,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                                .clickable {

                                    when{
                                        state.details.isUserCreator -> {
                                            onAction(AgendaItemAction.Delete)
                                        }
                                        state.details.isGoingUser -> {
                                            onAction(AgendaItemAction.Leave)
                                        }
                                        else -> {
                                            onAction(AgendaItemAction.Join)
                                        }
                                    }
                                }
                        )
                    }
                    else ->{
                        Text(
                            text = stringResource(id = R.string.delete_item, agendaItemName).uppercase(),
                            style = MaterialTheme.typography.titleSmall,
                            color = TaskyGray,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                                .clickable { onAction(AgendaItemAction.Delete) }
                        )
                    }
                }

            }
        }
    }
}


@Preview
@Composable
fun AgendaItemScreenPreview() {
    TaskyTheme {
        AgendaItemScreen(
            state = AgendaItemState(
                isEditing = true,
                title = "Title",
                description = "",
                startDateTime = ZonedDateTime.now(),
                details = AgendaItemDetailsUi.EventDetails(
                    finishDate = ZonedDateTime.now(),
                    host = "Host",
                    isUserCreator = true,
                    attendees = emptyList(),
                    emailPatterValidator =  object : PatternValidator {
                        override fun matches(value: String): Boolean {
                            return true
                        }
                    },
                    isGoingUser = true
                ),
                alarm = AlarmOptions.MINUTES_TEN
            ),
            onAction = {}
        )
    }
}