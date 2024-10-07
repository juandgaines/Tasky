package com.juandgaines.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juandgaines.auth.domain.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
):ViewModel() {
    var state by mutableStateOf(LoginState())
        private set

    private val eventChannel = Channel<LoginEvents>()
    val events = eventChannel.receiveAsFlow()

    private val email = snapshotFlow { state.email.text }
    private val password = snapshotFlow { state.password.text }

    init {
        combine(email,password){ email, password ->
        // TODO: validate email.


        }.launchIn(viewModelScope)
    }

}