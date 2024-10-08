package com.juandgaines.auth.presentation.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juandgaines.auth.domain.AuthRepository
import com.juandgaines.auth.domain.UserDataValidator
import com.juandgaines.auth.presentation.login.LoginEvents
import com.juandgaines.core.domain.util.DataError
import com.juandgaines.core.domain.util.Result.Error
import com.juandgaines.core.domain.util.Result.Success
import com.juandgaines.core.presentation.ui.UiText
import com.juandgaines.core.presentation.ui.asUiText
import com.juandgaines.presentation.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userDataValidator: UserDataValidator
):ViewModel(){

    private val eventChannel = Channel<RegisterEvents>()
    val events = eventChannel.receiveAsFlow()

    var state by mutableStateOf(RegisterState())
        private set

    init {
        snapshotFlow { state.fullName.text}
            .onEach { fullName ->
                val isNameValid = userDataValidator.isValidName(fullName.toString())
                state = state.copy(
                    isNameValid =isNameValid ,
                    canRegister = state.isEmailValid &&
                        state.passWordValidationState.isValidPassword &&
                        isNameValid &&
                        !state.isRegistering
                )
            }
            .launchIn(viewModelScope)

        snapshotFlow { state.email.text}
            .onEach { email ->
                val isEmailValid = userDataValidator.isValidEmail(email.toString())
                state = state.copy(
                    isEmailValid = userDataValidator.isValidEmail(email.toString()),
                    canRegister = isEmailValid &&
                        state.passWordValidationState.isValidPassword &&
                        state.isNameValid &&
                        !state.isRegistering
                )
            }
            .launchIn(viewModelScope)

        snapshotFlow { state.password.text}
            .onEach { password ->
                val isValidPassword = userDataValidator.validatePassword(password.toString())
                state = state.copy(
                    passWordValidationState = isValidPassword,
                    canRegister = state.isEmailValid &&
                        !state.isRegistering &&
                        isValidPassword.isValidPassword &&
                        state.isNameValid
                )
            }.launchIn(viewModelScope)


    }

    fun onAction(event: RegisterAction) {
        viewModelScope.launch {
            when(event) {
                is RegisterAction.OnRegisterClick -> {
                    state = state.copy(isRegistering = true)
                    val result = authRepository.register(
                        email = state.email.text.toString().trim(),
                        password = state.password.text.toString(),
                        fullName = state.fullName.text.toString()
                    )
                    state = state.copy(isRegistering = false)
                    when(result){
                        is Error -> {
                            if (result.error == DataError.Network.CONFLICT){
                                eventChannel.send(
                                    RegisterEvents.Error(
                                        UiText.StringResource(
                                            R.string.error_email_exist
                                        )
                                    )
                                )
                            } else {
                                eventChannel.send(
                                    RegisterEvents.Error(
                                        result.error.asUiText()
                                    )
                                )
                            }
                        }
                        is Success ->{
                            eventChannel.send(RegisterEvents.RegistrationSuccess)
                        }
                    }
                }
                is RegisterAction.OnTogglePassWordVisibility -> {
                    state = state.copy(isPasswordVisible = !state.isPasswordVisible)

                }
                else -> Unit
            }
        }
    }
}