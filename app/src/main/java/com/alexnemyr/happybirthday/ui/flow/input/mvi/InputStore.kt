package com.alexnemyr.happybirthday.ui.flow.input.mvi

import com.alexnemyr.domain.view_state.UserState
import com.arkivanov.mvikotlin.core.store.Store

interface InputStore : Store<InputStore.Intent, InputStore.State, InputStore.Label> {

    sealed class Action {
        data class UpdateUser(val user: UserState): Action()
    }

    sealed class Intent {
        data class Edit(val user: UserState) : Intent()
    }

    sealed class Label {
        data object NavigateToAnniversary : Label()
    }

    sealed class State {
        data object Progress : State()
        data class Error(val exception: Throwable) : State()
        data class Data(
            var name: String?,
            var date: String?,
            var uri: String?
        ) : State()
    }

    sealed class Message {
        data object Progress : Message()
        data class UserData(val result: UserState): Message()
    }
}