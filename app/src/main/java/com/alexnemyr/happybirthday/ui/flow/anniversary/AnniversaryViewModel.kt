package com.alexnemyr.happybirthday.ui.flow.anniversary

import androidx.lifecycle.ViewModel
import com.alexnemyr.happybirthday.TAG
import com.alexnemyr.happybirthday.ui.common.UserState
import com.alexnemyr.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber

class AnniversaryViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val mutableState: MutableStateFlow<UserState> = MutableStateFlow(
        UserState(
            name = userRepository.user.name,
            date = userRepository.user.date,
            uriPath = userRepository.user.uri
        )
    )
    val state: StateFlow<UserState> = mutableState

    init {
        val repo = userRepository.user
        Timber.tag(TAG).e("InputViewModel -> init $repo")
    }
}