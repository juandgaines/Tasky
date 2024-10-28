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
import com.juandgaines.agenda.domain.agenda.AgendaItems.Reminder
import com.juandgaines.agenda.domain.agenda.AgendaItems.Task
import com.juandgaines.agenda.presentation.agenda_item.AlarmOptions.DAY_ONE
import com.juandgaines.agenda.presentation.home.componets.Check
import com.juandgaines.agenda.presentation.home.AgendaCardMenuOperations
import com.juandgaines.agenda.presentation.R
import com.juandgaines.core.presentation.designsystem.MoreHor
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
    date: String,
    agendaItem: AgendaItems
) {
    val colorBackground= when (agendaItem){
        is Task ->MaterialTheme.colorScheme.primary
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

    var isMenuExpanded by remember { mutableStateOf(false) }

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
                    modifier = Modifier.pointerInput(true){
                        detectTapGestures (
                            onTap = {
                                isMenuExpanded = true
                            }
                        )
                    }
                )

                DropdownMenu(
                    expanded = isMenuExpanded,
                    onDismissRequest = { isMenuExpanded = false },
                ) {
                    DropdownMenuItem(
                        onClick = {
                            onMenuItemClick(AgendaCardMenuOperations.Open(agendaItem))
                        },
                        text = { Text(stringResource(R.string.open)) }
                    )
                    DropdownMenuItem(
                        onClick = {
                            onMenuItemClick(AgendaCardMenuOperations.Edit(agendaItem))
                        },
                        text = { Text(stringResource(R.string.edit)) }
                    )
                    DropdownMenuItem(
                        onClick = {
                            onMenuItemClick(AgendaCardMenuOperations.Delete(agendaItem))
                        },
                        text = { Text(stringResource(R.string.delete)) }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = date,
                style = MaterialTheme.typography.labelSmall,
                color = colorSecondaryText
            )
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
            date = "Mar 5, 10:00",
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
            date = "Mar 5, 10:00",
            agendaItem = Reminder("1","Title", "Description",
                ZonedDateTime.now(), ZonedDateTime.now()),
            onCheckClick = {},
            onClickItem = {},
            onMenuItemClick = {}
        )
    }
}