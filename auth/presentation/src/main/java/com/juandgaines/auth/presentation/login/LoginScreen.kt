@file:OptIn(ExperimentalMaterial3Api::class)

package com.juandgaines.auth.presentation.login

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.juandgaines.auth.presentation.login.LoginAction.OnRegisterClick
import com.juandgaines.auth.presentation.login.LoginEvents.LoginSuccess
import com.juandgaines.core.presentation.designsystem.TaskyTheme
import com.juandgaines.core.presentation.designsystem.components.TaskyActionButton
import com.juandgaines.core.presentation.designsystem.components.TaskyPasswordEditTextField
import com.juandgaines.core.presentation.designsystem.components.TaskyScaffold
import com.juandgaines.core.presentation.designsystem.components.TaskyTextField
import com.juandgaines.core.presentation.designsystem.components.TaskyToolbar
import com.juandgaines.core.presentation.ui.ObserveAsEvents
import com.juandgaines.presentation.R

@Composable
fun LoginScreenRoot(
    viewModel: LoginViewModel,
    onLoginSuccess: () -> Unit,
    onSingUpClick: () -> Unit
) {
    val state = viewModel.state
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    ObserveAsEvents(
        flow = viewModel.events
    ) {
        when (val event = it) {
            is LoginSuccess -> {
                keyboardController?.hide()
                Toast.makeText(
                    context,
                    R.string.you_are_logged_in,
                    Toast.LENGTH_SHORT
                ).show()
                onLoginSuccess()
            }
            is LoginEvents.Error -> {
                keyboardController?.hide()
                Toast.makeText(
                    context,
                    event.message.asString(context),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    LoginScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is OnRegisterClick -> onSingUpClick()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
fun LoginScreen(
    state: LoginState,
    onAction: (LoginAction) -> Unit
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
        },

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            TaskyTextField(
                state = state.email,
                endIcon = null,
                hint = stringResource(R.string.email_hint),
                modifier = Modifier
                    .fillMaxWidth()
            )

            TaskyPasswordEditTextField(
                state = state.password,
                hint = stringResource(R.string.password_hint),
                isPasswordVisible = state.isPasswordVisible,
                onTogglePasswordVisibility = {
                    onAction(LoginAction.OnTogglePasswordVisibility)
                },
                modifier = Modifier
                    .fillMaxWidth()
            )

            TaskyActionButton(
                text = stringResource(R.string.login_button),
                isLoading = state.isLoggingIn,
            ) {
                onAction(LoginAction.OnLoginClick)
            }
        }
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