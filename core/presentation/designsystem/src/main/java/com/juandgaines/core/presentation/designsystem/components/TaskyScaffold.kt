@file:OptIn(ExperimentalMaterial3Api::class)

package com.juandgaines.core.presentation.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.juandgaines.core.presentation.designsystem.BackIcon
import com.juandgaines.core.presentation.designsystem.CloseIcon
import com.juandgaines.core.presentation.designsystem.EditIcon
import com.juandgaines.core.presentation.designsystem.TaskyTheme

@Composable
fun TaskyScaffold(
    modifier: Modifier = Modifier,
    fabPosition: FabPosition = FabPosition.End,
    topAppBar: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    content : @Composable (PaddingValues) -> Unit,

) {
    Scaffold(
        modifier = modifier,
        topBar = topAppBar,
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = fabPosition,
        contentColor  = MaterialTheme.colorScheme.background,
        containerColor = MaterialTheme.colorScheme.background,
    ) { paddingValues ->
        Box (
            modifier = Modifier
                .padding(top = paddingValues.calculateTopPadding())
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colorScheme.surface
                )
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(
                            topStart = 32.dp,
                            topEnd = 32.dp,
                            bottomStart = 0.dp,
                            bottomEnd = 0.dp
                        )
                    )
                    .padding(16.dp),
            ) {
                content(paddingValues)
            }
        }
    }
}

@Composable
@Preview
fun TaskyScaffoldPreview() {
    TaskyTheme {
        TaskyScaffold(
            topAppBar = {
                TaskyToolbar(
                    startContent = {
                        Icon(
                            imageVector = CloseIcon,
                            contentDescription = "Back",
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    centerContent = {
                        Text(
                            text = "01 MARCH 2022",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    endContent = {
                        Icon(
                            imageVector = EditIcon,
                            contentDescription = "Back",
                            modifier = Modifier.size(25.dp),
                        )
                    }
                )
            },
            content = { padding->
                Column (
                    modifier = Modifier
                        .fillMaxSize()
                ){
                    Text(
                        text = "Hello World",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            },
            fabPosition = FabPosition.Start,
            floatingActionButton = {
                TaskyFAB(
                    onClick = {},
                    icon = BackIcon
                )
            }
        )
    }
}