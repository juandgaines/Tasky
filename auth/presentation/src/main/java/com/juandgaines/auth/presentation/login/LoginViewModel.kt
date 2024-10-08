package com.juandgaines.auth.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juandgaines.auth.domain.AuthRepository
import com.juandgaines.auth.domain.UserDataValidator
import com.juandgaines.core.domain.util.DataError.Network
import com.juandgaines.core.domain.util.Result.Error
import com.juandgaines.core.domain.util.Result.Success
import com.juandgaines.core.presentation.ui.UiText
import com.juandgaines.core.presentation.ui.asUiText
import com.juandgaines.presentation.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userDataValidator: UserDataValidator
):ViewModel() {
    var state by mutableStateOf(LoginState())
        private set

    private val eventChannel = Channel<LoginEvents>()
    val events = eventChannel.receiveAsFlow()

    init {
        snapshotFlow { state.email.text }
            .onEach {
                state = state.copy(
                    isEmailValid = userDataValidator.isValidEmail(
                        it.toString().trim()
                    ),
                    canLogin = state.isEmailValid && state.password.text.isNotEmpty()
                )
            }.stateIn(
                viewModelScope,
                started = SharingStarted.Eagerly,
                ""
            )
        snapshotFlow { state.password.text }
            .onEach {
                state = state.copy(
                    isEmailValid = userDataValidator.isValidEmail(
                        it.toString().trim()
                    ),
                    canLogin = state.isEmailValid && state.password.text.isNotEmpty()
                )
            }.stateIn(
                viewModelScope,
                started = SharingStarted.Eagerly,
                ""
            )
    }
    fun onAction(event: LoginAction) {
        viewModelScope.launch {
            when(event) {
                is LoginAction.OnTogglePasswordVisibility -> {
                    state = state.copy(isPasswordVisible = !state.isPasswordVisible)
                }
                is LoginAction.OnLoginClick -> {
                    state = state.copy(isLoggingIn = true)
                    val response = authRepository.login(
                        state.email.text.toString()
                        ,state.password.text.toString()
                    )
                    state = state.copy(isLoggingIn = false)
                    when(response){
                        is Error -> {
                            if (response.error == Network.UNAUTHORIZED){
                                state = state.copy(isError = true)
                                eventChannel.send(
                                    LoginEvents.Error(
                                        UiText.StringResource(
                                            R.string.error_email_password_incorrect
                                        )
                                    )
                                )
                            } else {
                                eventChannel.send(
                                    LoginEvents.Error(
                                        response.error.asUiText()
                                    )
                                )
                            }
                        }
                        is Success -> {
                            state = state.copy(isError = false)
                            eventChannel.send(LoginEvents.LoginSuccess)
                        }
                    }
                }
                else -> Unit
            }
        }
    }
}