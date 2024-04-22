package com.alexnemyr.happybirthday.ui.flow.anniversary

import androidx.lifecycle.ViewModel
import com.alexnemyr.domain.view_state.UserState
import com.alexnemyr.usecase.SaveUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AnniversaryViewModel(
    private val userUseCase: SaveUserUseCase
) : ViewModel() {

    private val mutableState: MutableStateFlow<UserState> = MutableStateFlow(
        UserState(
            name = "",
            date = "",
            uriPath = ""
        )
    )
    val state: StateFlow<UserState> = mutableState

}