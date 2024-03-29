package com.alexnemyr.happybirthday.ui.input

import android.net.Uri
import androidx.compose.runtime.MutableState

data class InputState(
    val capturedImageUri: MutableState<Uri>,
    val name: MutableState<String>,
    val date: MutableState<Long>
)
