package com.juandgaines.agenda.presentation.home.componets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.juandgaines.core.presentation.designsystem.TaskyLight
import com.juandgaines.core.presentation.designsystem.TaskyLightBlue
import com.juandgaines.core.presentation.designsystem.TaskyTheme

@Composable
fun ProfileIcon(
    modifier: Modifier = Modifier,
    initials: String
) {
    Box (
        modifier = modifier
            .background(
                color = TaskyLight,
                shape = CircleShape
            ).width(IntrinsicSize.Min),
        contentAlignment = Alignment.Center,
    ){
        Text(
            text = initials.uppercase(),
            color = TaskyLightBlue,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(4.dp)
        )
    }
}

@Composable
@Preview
fun ProfileIconPreview() {
    TaskyTheme {
        ProfileIcon(
            initials = "JD"
        )
    }
}