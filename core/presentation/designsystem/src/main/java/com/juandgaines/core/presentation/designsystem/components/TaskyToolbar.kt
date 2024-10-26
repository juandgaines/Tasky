@file:OptIn(ExperimentalMaterial3Api::class)

package com.juandgaines.core.presentation.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.juandgaines.core.presentation.designsystem.CloseIcon
import com.juandgaines.core.presentation.designsystem.EditIcon
import com.juandgaines.core.presentation.designsystem.TaskyTheme

@Composable
fun TaskyToolbar(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
    backNavigation: @Composable () -> Unit = {} ,
    actions: @Composable RowScope.() -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
) {
    TopAppBar(
        title = {
            content()
        },
        navigationIcon = {
            backNavigation()
        },
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        actions = actions,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
    )
}

@Preview
@Composable
fun TaskyToolbarPreview() {
    TaskyTheme {
        TaskyToolbar(
            backNavigation = {
                Icon(
                    imageVector = CloseIcon,
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(24.dp)
                        .offset(x = 16.dp)
                )
            },
            content = {
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ){
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "01 MARCH 2022",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        imageVector = EditIcon,
                        contentDescription = "Back",
                        modifier = Modifier.size(25.dp),
                    )
                }
            },
        )
    }
}