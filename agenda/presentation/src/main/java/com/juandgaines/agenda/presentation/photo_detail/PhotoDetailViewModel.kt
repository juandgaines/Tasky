package com.juandgaines.agenda.presentation.photo_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.juandgaines.core.presentation.navigation.ScreenNav
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PhotoDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
): ViewModel() {


    private var _state  = MutableStateFlow(PhotoState())
    private val photoStateData =  savedStateHandle.toRoute<ScreenNav.PhotoDetail>()

    var state = _state
        .onStart {
            _state.value = PhotoState(
                url = photoStateData.photoUrl
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = PhotoState()
        )
}