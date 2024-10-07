package com.juandgaines.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.juandgaines.auth.domain.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel

@HiltViewModel
class LoginViewModel(
    private val authRepository: AuthRepository
) {
    var state by mutableStateOf(LoginState())
        private set

    private val eventChannel = Channel<LoginEvents>()

}