package com.juandgaines.agenda.presentation.home.componets

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.juandgaines.core.presentation.designsystem.TaskyTheme

@Composable
fun Check(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit = {},
    isDone: Boolean = false
) {
    Box(
        modifier = modifier
            .size(
                20.dp
            )
            .clip(
                shape = CircleShape
            )
            .border(
                width = 1.dp,
                color = color,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        if (isDone) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = color,
                modifier = Modifier
                    .size(16.dp)
                    .padding(2.dp)
            )
        }
    }
}

@Composable
@Preview (backgroundColor = 0xFFFFFFFF)
fun CheckPreview() {
    TaskyTheme {
        Check()
    }
}