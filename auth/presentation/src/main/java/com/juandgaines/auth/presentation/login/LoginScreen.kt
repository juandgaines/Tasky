@file:OptIn(ExperimentalMaterial3Api::class)

package com.juandgaines.auth.presentation.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.juandgaines.core.presentation.designsystem.TaskyTheme
import com.juandgaines.core.presentation.designsystem.components.TaskyScaffold
import com.juandgaines.core.presentation.designsystem.components.TaskyToolbar
import com.juandgaines.presentation.R

@Composable
fun LoginScreenRoot(
    viewModel: LoginViewModel
) {
    val state = viewModel.state
    val events = viewModel.events
    LoginScreen(
        state = state,
        onAction = { event ->
            viewModel.onAction(event)
        }
    )
}

@Composable
fun LoginScreen(
    state: LoginState,
    onAction: (LoginEvents) -> Unit
) {
    TaskyScaffold(
        topAppBar = {
            TaskyToolbar(
                content = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = stringResource(R.string.login_title),
                            style = MaterialTheme.typography.displayLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                        )
                        Spacer(modifier = Modifier.weight(1f))
                    }
                },
                modifier  = Modifier
                    .height(120.dp)
            )
        }
    ) {

    }
}

@Composable
@Preview
fun LoginScreenRootPreview() {
    TaskyTheme {

        LoginScreen(
            state = LoginState(),
            onAction = {}
        )
    }
}