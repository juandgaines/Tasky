package com.juandgaines.agenda.presentation.home.componets.agenda_cards

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.juandgaines.agenda.domain.agenda.AgendaItems
import com.juandgaines.agenda.domain.agenda.AgendaItems.Event
import com.juandgaines.agenda.domain.agenda.AgendaItems.Reminder
import com.juandgaines.agenda.domain.agenda.AgendaItems.Task
import com.juandgaines.agenda.domain.utils.toFormattedSingleDateTime
import com.juandgaines.agenda.presentation.agenda_item.AlarmOptions.DAY_ONE
import com.juandgaines.agenda.presentation.home.componets.Check
import com.juandgaines.agenda.presentation.home.AgendaCardMenuOperations
import com.juandgaines.agenda.presentation.R
import com.juandgaines.core.presentation.designsystem.MoreHor
import com.juandgaines.core.presentation.designsystem.TaskyLightGreen
import com.juandgaines.core.presentation.designsystem.TaskyTheme
import java.time.ZonedDateTime

@Composable
fun AgendaCard(
    modifier: Modifier = Modifier,
    title: String,
    isDone: Boolean = false,
    onCheckClick:( () -> Unit )? = null,
    onClickItem: (() -> Unit),
    onMenuItemClick: (AgendaCardMenuOperations) -> Unit,
    description: String,
    agendaItem: AgendaItems
) {
    val colorBackground= when (agendaItem){
        is Task ->MaterialTheme.colorScheme.primary
        is Event -> TaskyLightGreen
        else -> MaterialTheme.colorScheme.primaryContainer
    }

    val colorPrimaryText= when (agendaItem){
        is Task ->MaterialTheme.colorScheme.onSurface
        else -> MaterialTheme.colorScheme.surface
    }

    val colorSecondaryText= when (agendaItem){
        is Task ->MaterialTheme.colorScheme.onSurface
        else -> MaterialTheme.colorScheme.onPrimary
    }
    val colorCheck = when (agendaItem){
        is Task ->MaterialTheme.colorScheme.onSurface
        else -> MaterialTheme.colorScheme.surface
    }

    var isMenuExpanded by rememberSaveable { mutableStateOf(false) }

    Row (
        modifier
            .background(
                color = colorBackground,
                shape = RoundedCornerShape(16.dp)
            )
            .height(124.dp)
            .padding(16.dp)
            .clickable {
                onClickItem()
            },
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ){
        Box(
            modifier = Modifier.fillMaxHeight()
        ){
            Check(
                isDone = isDone,
                modifier = Modifier.then(
                    if (onCheckClick != null)
                        Modifier.clickable { onCheckClick() }
                    else
                        Modifier
                ),
                color = colorCheck
            )
        }
        Column (
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            Text(
                text = title,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.bodyLarge.copy(
                    textDecoration = if (isDone)
                        TextDecoration.LineThrough
                    else
                        TextDecoration.None
                ),
                textAlign = TextAlign.Start,
                color = colorPrimaryText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis

            )
            Text(
                text = description,
                fontWeight = FontWeight.Light,
                style = MaterialTheme.typography.bodyMedium,
                color = colorSecondaryText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "${agendaItem.date.toFormattedSingleDateTime()} " +
                    if (agendaItem.dateEnd !=null) "- "+agendaItem.dateEnd?.toFormattedSingleDateTime() else "",
                style = MaterialTheme.typography.labelSmall,
                color = colorSecondaryText,
                modifier = Modifier.align(Alignment.End)
            )

        }
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box {
                Icon(
                    imageVector = MoreHor,
                    contentDescription = null,
                    tint = colorSecondaryText,
                    modifier = Modifier.clickable {
                        isMenuExpanded = true
                    }
                )

                DropdownMenu(
                    expanded = isMenuExpanded,
                    onDismissRequest = { isMenuExpanded = false },
                ) {
                    DropdownMenuItem(
                        onClick = {
                            isMenuExpanded = false
                            onMenuItemClick(AgendaCardMenuOperations.Open(
                                agendaItem
                            ))
                        },
                        text = { Text(stringResource(R.string.open)) }
                    )
                    DropdownMenuItem(
                        onClick = {
                            isMenuExpanded = false
                            onMenuItemClick(AgendaCardMenuOperations.Edit(agendaItem))
                        },
                        text = { Text(stringResource(R.string.edit)) }
                    )
                    DropdownMenuItem(
                        onClick = {
                            isMenuExpanded = false
                            onMenuItemClick(AgendaCardMenuOperations.Delete(agendaItem))
                        },
                        text = { Text(stringResource(R.string.delete)) }
                    )
                }
            }

        }

    }

}

@Preview
@Composable
fun TaskCardPreview() {
    TaskyTheme {
        AgendaCard(
            title = "Title",
            isDone = true,
            description = "Description",
            agendaItem = Task("1","Title", "Description"
                ,ZonedDateTime.now(),
                ZonedDateTime.now(),
                false)
            ,
            onCheckClick = {},
            onClickItem = {},
            onMenuItemClick = {}
        )
    }
}

@Preview
@Composable
fun ReminderCardPreview() {
    TaskyTheme {
        AgendaCard(
            title = "Title",
            description = "Description",
            agendaItem = Reminder("1","Title", "Description",
                ZonedDateTime.now(), ZonedDateTime.now()),
            onCheckClick = {},
            onClickItem = {},
            onMenuItemClick = {}
        )
    }
}

@Preview
@Composable
fun EventCardPreview() {
    TaskyTheme {
        AgendaCard(
            title = "Title",
            description = "Description",
            agendaItem = Event("1","Title", "Description",
                ZonedDateTime.now(), ZonedDateTime.now(), ZonedDateTime.now(),
                "Host", true),
            onCheckClick = {},
            onClickItem = {},
            onMenuItemClick = {}
        )
    }
}