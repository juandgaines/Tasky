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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.juandgaines.agenda.domain.utils.toFormattedTime
import com.juandgaines.core.presentation.designsystem.BellIcon
import com.juandgaines.core.presentation.designsystem.TaskyGray
import com.juandgaines.core.presentation.designsystem.TaskyLight2
import com.juandgaines.core.presentation.designsystem.TaskyTheme
import java.time.ZonedDateTime

@Composable
fun AlarmSection(
    modifier: Modifier = Modifier,
    alarmText: String,
    isEditing: Boolean,
    onSelectAlarmTime: () -> Unit,
){
    Row (
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (isEditing)
                    Modifier.clickable {
                        onSelectAlarmTime()
                    }
                else Modifier
            ),
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
            text = alarmText,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onSecondary,
        )
        Spacer(modifier = Modifier.weight(1f))
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
fun AlarmSectionPreview() {
    TaskyTheme {
         AlarmSection(
             alarmText = "30 minutes before",
             isEditing = true,
             onSelectAlarmTime = {}
         )
    }
}