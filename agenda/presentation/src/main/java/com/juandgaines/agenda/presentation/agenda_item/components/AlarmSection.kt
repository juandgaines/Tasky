package com.juandgaines.agenda.presentation.agenda_item.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.juandgaines.agenda.presentation.agenda_item.AlarmOptions
import com.juandgaines.agenda.presentation.R
import com.juandgaines.core.presentation.designsystem.BellIcon
import com.juandgaines.core.presentation.designsystem.TaskyGray
import com.juandgaines.core.presentation.designsystem.TaskyLight2
import com.juandgaines.core.presentation.designsystem.TaskyTheme

@Composable
fun AlarmSection(
    modifier: Modifier = Modifier,
    alarm: AlarmOptions,
    isEditing: Boolean,
    onSelectAlarmTime: (AlarmOptions) -> Unit,
){
    var isExpanded  by remember { mutableStateOf(false) }
    val options = listOf(
        AlarmOptions.MINUTES_TEN to stringResource(R.string.ten_minutes_before),
        AlarmOptions.MINUTES_THIRTY to stringResource(R.string.thirty_minutes_before),
        AlarmOptions.HOUR_ONE to stringResource(R.string.one_hour_before),
        AlarmOptions.HOUR_SIX to  stringResource(R.string.six_hours_before),
        AlarmOptions.DAY_ONE to  stringResource(R.string.one_day_before)
    )

    val alarmText = options.find { it.first == alarm }?.second ?: ""

    Row (
        modifier = modifier
            .fillMaxWidth()
            .clickable (enabled = isEditing){
                isExpanded = true
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),

        ) {

        Box(
            modifier = Modifier
                .size(30.dp)
                .background(
                    color = TaskyLight2,
                    shape = RoundedCornerShape(8.dp)
                ),
            contentAlignment = Alignment.Center
        ){
            Icon(
                imageVector = BellIcon,
                contentDescription = null,
                tint = TaskyGray
            )
        }

        Text(
            text = alarmText ,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onSecondary,
        )
        Spacer(modifier = Modifier.weight(1f))
        if (isEditing) {
            Box {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Edit",
                    tint = MaterialTheme.colorScheme.onSecondary
                )
                DropdownMenu(
                    expanded = isExpanded,
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.surface
                        ),
                    onDismissRequest = {
                        isExpanded = false
                    }
                ) {
                    AlarmOptions.entries.forEach { alarmOption->
                        DropdownMenuItem(
                            onClick = {
                                onSelectAlarmTime(alarmOption)
                                isExpanded = false
                            },
                            text = {
                                Text(
                                    style = MaterialTheme.typography.bodySmall,
                                    text = options.find { it.first == alarmOption }?.second ?: "",
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
fun AlarmSectionPreview() {
    TaskyTheme {
         AlarmSection(
             alarm = AlarmOptions.MINUTES_TEN,
             isEditing = true,
             onSelectAlarmTime = {}
         )
    }
}