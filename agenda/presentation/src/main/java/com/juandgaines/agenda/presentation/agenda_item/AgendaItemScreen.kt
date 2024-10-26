@file:OptIn(ExperimentalMaterial3Api::class)

package com.juandgaines.agenda.presentation.agenda_item

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.juandgaines.agenda.domain.agenda.AgendaItem
import com.juandgaines.agenda.domain.agenda.AgendaType
import com.juandgaines.agenda.domain.agenda.AgendaType.Event
import com.juandgaines.agenda.domain.agenda.AgendaType.Reminder
import com.juandgaines.agenda.domain.task.Task
import com.juandgaines.agenda.domain.utils.toFormattedDate
import com.juandgaines.agenda.domain.utils.toFormattedTime
import com.juandgaines.agenda.presentation.R
import com.juandgaines.core.presentation.designsystem.CloseIcon
import com.juandgaines.core.presentation.designsystem.EditIcon
import com.juandgaines.core.presentation.designsystem.TaskyGray
import com.juandgaines.core.presentation.designsystem.TaskyGreen
import com.juandgaines.core.presentation.designsystem.TaskyLight
import com.juandgaines.core.presentation.designsystem.TaskyLightGreen
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
            AgendaItemType(
                agendaType = state.agendaType
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
                date = state.agendaItem.date,
                isEditing = state.isEditing,
                onEditStartDate = {

                },
                onEditStartTime = {

                }
            )
        }
    }
}
@Composable
fun AgendaItemType(
    modifier: Modifier = Modifier,
    agendaType: AgendaType
){
    Row (
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Box (
            modifier = Modifier
                .size(20.dp)
                .clip(
                    RoundedCornerShape(4.dp)
                )
                .then(
                    when (agendaType){
                        Event ->
                            Modifier.background(
                                color = TaskyLightGreen
                            )
                        Reminder ->
                            Modifier.background(
                                color = TaskyGray
                            )
                        AgendaType.Task ->
                            Modifier.background(
                                color = TaskyGreen
                            )
                    }
                )
        )
        Text(
            text = when (agendaType){
                Event -> stringResource(id = R.string.event)
                Reminder -> stringResource(id = R.string.reminder)
                AgendaType.Task -> stringResource(id = R.string.task)
            },
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onPrimary,
        )
    }
}
@Preview(
    showBackground = true
)
@Composable
fun AgendaItemTypePreview() {
    TaskyTheme {
        AgendaItemType(agendaType = AgendaType.Task)
    }
}


@Composable
fun TitleSection(
    modifier: Modifier = Modifier,
    title: String,
    isEditing: Boolean = true,
    onEditTitle: () -> Unit = {}
){
    Row (
        modifier = modifier
            .then(
                if (isEditing)
                    Modifier.clickable {
                        onEditTitle()
                    }
                else Modifier
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),

    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(
                   CircleShape
                )
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.surface,
                    shape = CircleShape
                )
        )
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.onSecondary,
            modifier = Modifier.weight(1f)
        )
        if (isEditing) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Edit",
                tint = MaterialTheme.colorScheme.onSecondary
            )
        }

    }
}
@Preview(
    showBackground = true
)
@Composable
fun TitleSectionPreview() {
    TaskyTheme {
        TitleSection(title = "Title")
    }
}

@Composable
fun DescriptionSection(
    modifier: Modifier = Modifier,
    description: String ?,
    isEditing: Boolean,
    onEditTitle: () -> Unit
){
    Row (
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (isEditing)
                    Modifier.clickable {
                        onEditTitle()
                    }
                else Modifier
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),

        ) {
        Text(
            text = description ?: stringResource(id = R.string.no_description),
            style = MaterialTheme.typography.bodySmall,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            color = description?.let {
                MaterialTheme.colorScheme.onSecondary
            } ?: TaskyGray,
            modifier = Modifier.weight(1f)
        )
        if (isEditing) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Edit",
                tint = MaterialTheme.colorScheme.onSecondary
            )
        }
    }
}
@Preview(
    showBackground = true
)
@Composable
fun DescriptionSectionPreview() {
    TaskyTheme {
        DescriptionSection(
            description = "This is a description",
            isEditing = true,
            onEditTitle = {}
        )
    }
}

@Composable
fun StartDateSection(
    modifier: Modifier = Modifier,
    date: ZonedDateTime,
    isEditing: Boolean,
    onEditStartDate: () -> Unit,
    onEditStartTime: () -> Unit
){
    Row (
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (isEditing)
                    Modifier.clickable {
                        onEditStartDate()
                    }
                else Modifier
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,

        ) {

        Text(
            text = stringResource(id = R.string.from),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSecondary,
        )

        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.weight(1f)
                .then(
                    if (isEditing)
                        Modifier.clickable {
                            onEditStartDate()
                        }
                    else Modifier
                )
        ){
            Text(
                text = date.toFormattedTime(),
                style = MaterialTheme.typography.bodySmall,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSecondary,
            )
            if (isEditing) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Edit",
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = date.toFormattedDate(),
                style = MaterialTheme.typography.bodySmall,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.weight(1f)
            )
            if (isEditing) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Edit",
                    tint = MaterialTheme.colorScheme.onSecondary
                )
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
                description = null,
                agendaItem = Task(
                    id = "1",
                    title = "Title",
                    description = "Description",
                    isDone = false,
                    remindAt = ZonedDateTime.now(),
                    time = ZonedDateTime.now()
                ),
                agendaType = AgendaType.Task
            ),
            onAction = {}
        )
    }
}

data class AgendaItemState(
    val isEditing: Boolean = true,
    val title: String = "",
    val description: String? = null,
    val agendaItem: AgendaItem,
    val agendaType: AgendaType
)