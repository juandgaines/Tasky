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
import androidx.compose.ui.unit.dp
import com.juandgaines.agenda.presentation.R
import com.juandgaines.core.presentation.designsystem.TaskyGray
import com.juandgaines.core.presentation.designsystem.TaskyTheme

@Composable
fun DescriptionSection(
    modifier: Modifier = Modifier,
    description: String,
    isEditing: Boolean,
    canEditField: Boolean,
    onEditDescription: () -> Unit,
){
    Row (
        modifier = modifier
            .fillMaxWidth()
            .clickable(isEditing && canEditField) {
                onEditDescription()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),

        ) {
        Text(
            text = description.ifEmpty {
                stringResource(id = R.string.no_description)
            },
            style = MaterialTheme.typography.bodySmall,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            color = if (description.isEmpty())
                TaskyGray
            else
                MaterialTheme.colorScheme.onSecondary,
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
fun DescriptionSectionPreview() {
    TaskyTheme {
        DescriptionSection(
            description = "This is a description",
            isEditing = true,
            onEditDescription = {},
            canEditField = true
        )
    }
}