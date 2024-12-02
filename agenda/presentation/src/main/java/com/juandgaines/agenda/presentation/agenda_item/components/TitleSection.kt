package com.juandgaines.agenda.presentation.agenda_item.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.juandgaines.core.presentation.designsystem.TaskyTheme

@Composable
fun TitleSection(
    modifier: Modifier = Modifier,
    title: String,
    isEditing: Boolean = true,
    canEditField: Boolean,
    onEditTitle: () -> Unit = {},
){
    Row (
        modifier = modifier
            .clickable(isEditing && canEditField) {
                onEditTitle()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),

        ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(
                    CircleShape
                )
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.surface,
                    shape = CircleShape
                )
        )
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.onSecondary,
            modifier = Modifier.weight(1f)
        )
        if (isEditing && canEditField) {
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
fun TitleSectionPreview() {
    TaskyTheme {
        TitleSection(title = "Title", canEditField = false)
    }
}