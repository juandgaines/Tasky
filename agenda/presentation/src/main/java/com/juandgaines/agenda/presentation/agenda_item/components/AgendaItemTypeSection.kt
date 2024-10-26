package com.juandgaines.agenda.presentation.agenda_item.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.juandgaines.agenda.domain.agenda.AgendaType
import com.juandgaines.agenda.domain.agenda.AgendaType.Event
import com.juandgaines.agenda.domain.agenda.AgendaType.Reminder
import com.juandgaines.agenda.presentation.R
import com.juandgaines.core.presentation.designsystem.TaskyGray
import com.juandgaines.core.presentation.designsystem.TaskyGreen
import com.juandgaines.core.presentation.designsystem.TaskyLightGreen
import com.juandgaines.core.presentation.designsystem.TaskyTheme

@Composable
fun AgendaItemTypeSection(
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
fun AgendaItemTypeSectionPreview() {
    TaskyTheme {
        AgendaItemTypeSection(agendaType = AgendaType.Task)
    }
}

