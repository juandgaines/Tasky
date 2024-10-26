@file:OptIn(ExperimentalMaterial3Api::class)

package com.juandgaines.agenda.presentation.agenda_item

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.juandgaines.agenda.domain.agenda.AgendaItem
import com.juandgaines.agenda.domain.agenda.AgendaType
import com.juandgaines.agenda.domain.task.Task
import com.juandgaines.core.presentation.designsystem.CloseIcon
import com.juandgaines.core.presentation.designsystem.EditIcon
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