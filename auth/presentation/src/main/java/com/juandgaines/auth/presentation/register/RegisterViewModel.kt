package com.juandgaines.auth.presentation.register

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(

):ViewModel(){


    fun onAction(event: RegisterAction) {
        when(event) {
            is RegisterAction.OnRegisterClick -> {

            }
            is RegisterAction.OnTogglePassWordVisibility -> {

            }
            else -> Unit
        }
    }
}