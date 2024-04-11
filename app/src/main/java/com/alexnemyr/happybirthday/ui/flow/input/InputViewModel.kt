package com.alexnemyr.happybirthday.ui.flow.input

import android.net.Uri
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.alexnemyr.happybirthday.TAG
import com.alexnemyr.happybirthday.ui.common.BirthdayState
import com.alexnemyr.repository.UserRepository
import com.alexnemyr.repository.domain.UserDomain
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber

class InputViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    val mutableState: MutableStateFlow<BirthdayState> = MutableStateFlow(

        BirthdayState(
            capturedImageUri = mutableStateOf(Uri.EMPTY), //Uri.parse(userRepository.user.uri)
            name = mutableStateOf(userRepository.user.name ?: ""),
            date = mutableLongStateOf(userRepository.user.date?.toLongOrNull() ?: 2)
        )
    )
    val state: StateFlow<BirthdayState> = mutableState

    init {
        val repo = userRepository.user
        Timber.tag(TAG).e("InputViewModel -> init $repo")
    }

    fun saveInfo(
        name: String,
        date: String,
        uri: String
    ) {
        userRepository.saveUser(
            UserDomain(
                name = name,
                date = date,
                uri = uri
            )
        )
    }

}