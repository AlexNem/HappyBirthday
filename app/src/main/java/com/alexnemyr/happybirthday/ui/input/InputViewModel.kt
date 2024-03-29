package com.alexnemyr.happybirthday.ui.input

import android.net.Uri
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.alexnemyr.happybirthday.TAG
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber

class InputViewModel : ViewModel() {

    private val mutableState: MutableStateFlow<InputState> = MutableStateFlow(
        InputState(
            capturedImageUri = Uri.EMPTY,
            name = mutableStateOf(""),
            date = mutableLongStateOf(0)
        )
    )
    val state: StateFlow<InputState> = mutableState

    init {
        Timber.tag(TAG).e("InputViewModel -> init")
    }

}