package com.juandgaines.agenda.presentation.agenda_item.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.juandgaines.agenda.domain.utils.toFormattedDate
import com.juandgaines.agenda.domain.utils.toFormattedTime
import com.juandgaines.agenda.presentation.R
import com.juandgaines.core.presentation.designsystem.TaskyTheme
import java.time.ZonedDateTime

@Composable
fun DateSection(
    modifier: Modifier = Modifier,
    date: ZonedDateTime,
    title : String,
    isEditing: Boolean,
    onEditStartDate: () -> Unit,
    onEditStartTime: () -> Unit
){
    Row (
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,

        ) {

        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSecondary,
        )

        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.weight(1f)
                .clickable (enabled = isEditing) {
                    onEditStartTime()
                },
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
                .clickable (enabled = isEditing) {
                    onEditStartDate()
                }
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

@Preview(
    showBackground = true
)
@Composable
fun StartDateSectionPreview() {
    TaskyTheme {
        DateSection(
            date = ZonedDateTime.now(),
            isEditing = false,
            onEditStartDate = {},
            onEditStartTime = {},
            title = stringResource(id = R.string.from)
        )
    }
}