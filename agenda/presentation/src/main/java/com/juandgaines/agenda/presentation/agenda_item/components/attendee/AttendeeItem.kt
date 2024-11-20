package com.juandgaines.agenda.presentation.agenda_item.components.attendee

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.juandgaines.agenda.presentation.home.componets.ProfileIcon
import com.juandgaines.core.presentation.designsystem.TaskyBlack
import com.juandgaines.core.presentation.designsystem.TaskyDarkGray
import com.juandgaines.core.presentation.designsystem.TaskyGray
import com.juandgaines.core.presentation.designsystem.TaskyLight
import com.juandgaines.core.presentation.designsystem.TaskyLightBlue
import com.juandgaines.core.presentation.designsystem.TaskyTheme

@Composable
fun AttendeeItem(
    modifier: Modifier = Modifier,
    isCreator : Boolean = false,
    isEdition : Boolean = true
) {

    Row (
        modifier = modifier
            .background(
                color = TaskyLight,
                shape = RoundedCornerShape(
                    8.dp
                )
            ).padding(4.dp),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ){
        ProfileIcon(
            modifier = Modifier
                .padding(8.dp),
            initials = "JD", // TODO: Replace with real initials
            colorBackground = TaskyGray,
            colorLetters = TaskyLight
        )
        Text(
            text = "Juan David",
            color = TaskyDarkGray,
            style = MaterialTheme.typography.labelMedium,
        )
        Spacer(modifier = Modifier.weight(1f))
        when {
            isCreator -> {
                Text(
                    text = "Creator",
                    color = TaskyLightBlue,
                    style = MaterialTheme.typography.labelSmall,
                )
            }
            else -> {
                if (isEdition) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Edit",
                        tint = TaskyBlack
                    )
                }
            }

        }
        Spacer(modifier = Modifier.width(8.dp))
    }
}
@Composable
@Preview
fun PreviewAttendeeItem() {
    TaskyTheme {
        AttendeeItem()
    }
}