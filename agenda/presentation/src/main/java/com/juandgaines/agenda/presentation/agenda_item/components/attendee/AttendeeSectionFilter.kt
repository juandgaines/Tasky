package com.juandgaines.agenda.presentation.agenda_item.components.attendee

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.juandgaines.agenda.domain.agenda.Attendee
import com.juandgaines.agenda.presentation.R
import com.juandgaines.agenda.presentation.agenda_item.AttendeeFilter
import com.juandgaines.agenda.presentation.agenda_item.AttendeeFilter.ALL
import com.juandgaines.agenda.presentation.agenda_item.AttendeeFilter.GOING
import com.juandgaines.core.presentation.designsystem.TaskyGray
import com.juandgaines.core.presentation.designsystem.TaskyTheme
import com.juandgaines.core.presentation.ui.UiText.StringResource

@Composable
fun AttendeeSectionFilter(
    modifier: Modifier = Modifier,
    selectedFilter: AttendeeFilter = ALL,
    isEditing: Boolean,
    onSelectFilter: (AttendeeFilter) -> Unit,
    attendeesGoing: List<Attendee>,
    attendeesNotGoing: List<Attendee>,
) {
    Column (
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.visitors),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSecondary
        )

        Row (
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ){
            AttendeeFilter.entries.forEach { filter->
                FilterChip(
                    modifier = Modifier.weight(1f).clip(
                        RoundedCornerShape(16.dp)
                    ).padding(8.dp),
                    selected = selectedFilter == filter,
                    label = {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = StringResource(filter.resource).asString(),
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    },
                    onClick = {
                        onSelectFilter(filter)
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = TaskyGray,
                        selectedContainerColor = MaterialTheme.colorScheme.surface,
                    ),
                    border = null
                )
            }
        }
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {

            if (
                (selectedFilter == ALL || selectedFilter == GOING) &&
                attendeesGoing.isNotEmpty()
            ){
                item {
                    Text(
                        text = stringResource(R.string.going),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }
                items(attendeesGoing) { attendee ->
                    AttendeeItem(
                        attendee = attendee,
                        isEdition = isEditing
                    )
                }
            }

            if (
                (selectedFilter == ALL || selectedFilter == GOING) &&
                attendeesNotGoing.isNotEmpty()
            ){

                item {
                    Text(
                        text = stringResource(R.string.not_going),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }
                items(attendeesNotGoing) { attendee ->
                    AttendeeItem(
                        attendee = attendee,
                        isEdition = isEditing
                    )
                }
            }

        }

    }
}

@Composable
@Preview(
    showBackground = true
)
fun PreviewAttendeeSectionFilter() {
    TaskyTheme {
        AttendeeSectionFilter(
            selectedFilter = AttendeeFilter.ALL,
            onSelectFilter = {},
            attendeesNotGoing = listOf(),
            attendeesGoing = listOf(),
            isEditing = false
        )
    }
}