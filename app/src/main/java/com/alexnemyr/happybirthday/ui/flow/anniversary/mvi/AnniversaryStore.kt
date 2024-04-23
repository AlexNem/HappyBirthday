package com.alexnemyr.happybirthday.ui.flow.anniversary.mvi

import com.alexnemyr.domain.view_state.UserState
import com.arkivanov.mvikotlin.core.store.Store

interface AnniversaryStore : Store<AnniversaryStore.Intent, AnniversaryStore.State, AnniversaryStore.Label> {

    sealed class Action {
        data class UpdateUser(val user: UserState) : Action()
    }

    sealed class Intent {
        data object ShowInputScreen : Intent()
        data class Edit(val user: UserState) : Intent()
    }

    sealed class Label {
        data object NavigateToInput : Label()
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
        data class UserData(val result: UserState) : Message()
    }

}