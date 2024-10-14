package com.juandgaines.core.presentation.designsystem.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.juandgaines.core.presentation.designsystem.BackIcon
import com.juandgaines.core.presentation.designsystem.TaskyTheme

@Composable
fun TaskyFAB(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    icon:ImageVector,
    interactionSource: MutableInteractionSource? = null,
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        interactionSource=interactionSource
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null
        )
    }
}

@Composable
@Preview
fun TaskyFABPreview() {
    TaskyTheme {
        TaskyFAB(
            onClick = {},
            icon = BackIcon
        )
    }
}