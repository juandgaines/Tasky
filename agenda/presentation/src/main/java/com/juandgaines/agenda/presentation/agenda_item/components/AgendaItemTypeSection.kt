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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.juandgaines.agenda.presentation.agenda_item.AgendaItemDetails
import com.juandgaines.core.presentation.designsystem.TaskyGray
import com.juandgaines.core.presentation.designsystem.TaskyGreen
import com.juandgaines.core.presentation.designsystem.TaskyLightGreen
import com.juandgaines.core.presentation.designsystem.TaskyTheme

@Composable
fun AgendaItemTypeSection(
    modifier: Modifier = Modifier,
    agendaItemDetails: AgendaItemDetails?,
    agendaItemName:String
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
                    when (agendaItemDetails){
                        is AgendaItemDetails.TaskDetails -> Modifier.background(
                            color = TaskyGreen
                        )
                        is AgendaItemDetails.EventDetails -> Modifier.background(
                            color = TaskyLightGreen
                        )
                        else ->Modifier.background(
                            color = TaskyGray
                        )
                    }
                )
        )
        Text(
            text = agendaItemName,
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
        AgendaItemTypeSection(
            agendaItemDetails = AgendaItemDetails.ReminderDetails ,
            agendaItemName =""
        )
    }
}

