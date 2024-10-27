@file:OptIn(ExperimentalMaterial3Api::class)

package com.juandgaines.agenda.presentation.agenda_item

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.juandgaines.agenda.presentation.R
import com.juandgaines.agenda.presentation.agenda_item.components.AgendaItemTypeSection
import com.juandgaines.agenda.presentation.agenda_item.components.AlarmSection
import com.juandgaines.agenda.presentation.agenda_item.components.DescriptionSection
import com.juandgaines.agenda.presentation.agenda_item.components.StartDateSection
import com.juandgaines.agenda.presentation.agenda_item.components.TitleSection
import com.juandgaines.core.presentation.designsystem.CloseIcon
import com.juandgaines.core.presentation.designsystem.EditIcon
import com.juandgaines.core.presentation.designsystem.TaskyGray
import com.juandgaines.core.presentation.designsystem.TaskyLight
import com.juandgaines.core.presentation.designsystem.TaskyTheme
import com.juandgaines.core.presentation.designsystem.components.TaskyScaffold
import com.juandgaines.core.presentation.designsystem.components.TaskyToolbar
import java.time.ZonedDateTime

@Composable
fun AgendaItemScreenRoot(

) {

}

@Composable
fun AgendaItemScreen(
    state: AgendaItemState,
    onAction: (AgendaItemAction) -> Unit
) {
    val agendaItemName = when (state.details) {
        is AgendaItemDetails.ReminderDetails -> stringResource(id = R.string.reminder)
        is AgendaItemDetails.EventDetails -> stringResource(id = R.string.event)
        is AgendaItemDetails.TaskDetails -> stringResource(id = R.string.task)
    }
    TaskyScaffold (
        topAppBar = {
            TaskyToolbar(
                modifier = Modifier
                    .padding(
                        horizontal = 16.dp
                    ),
                backNavigation = {
                    Icon(
                        imageVector = CloseIcon,
                        contentDescription = "Close",
                    )
                },
                content = {
                    Text(
                        text = state.title,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                },
                actions = {
                    if (state.isEditing) {
                        Text(
                            text = "Save",
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.titleSmall,
                        )
                    }
                    else {
                        Icon(
                            imageVector = EditIcon,
                            contentDescription = "Edit",
                        )
                    }
                },
            )
        }
    ){
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ){
            AgendaItemTypeSection(
                agendaItemDetails = state.details
            )
            TitleSection(
                title = state.title
            )
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth(),
                color = TaskyLight
            )

            DescriptionSection(
                description = state.description,
                isEditing = state.isEditing,
                onEditTitle = {
                    onAction(AgendaItemAction.EditDescription(state.description))
                }
            )

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth(),
                color = TaskyLight
            )

            StartDateSection(
                date = state.startDateTime,
                isEditing = state.isEditing,
                onEditStartDate = {

                },
                onEditStartTime = {

                }
            )

            AlarmSection(
                alarm = state.alarm,
                isEditing = state.isEditing,
                onSelectAlarmTime = {}
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = stringResource(id = R.string.delete_item, agendaItemName).uppercase(),
                style = MaterialTheme.typography.titleSmall,
                color = TaskyGray,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            )
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
                details = AgendaItemDetails.ReminderDetails,
                alarm = AlarmOptions.MINUTES_TEN
            ),
            onAction = {}
        )
    }
}