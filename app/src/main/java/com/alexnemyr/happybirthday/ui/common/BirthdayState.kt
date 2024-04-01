package com.alexnemyr.happybirthday.ui.common

import android.net.Uri
import androidx.compose.runtime.MutableState

data class BirthdayState(
    val capturedImageUri: MutableState<Uri>,
    val name: MutableState<String>,
    val date: MutableState<Long>
)
