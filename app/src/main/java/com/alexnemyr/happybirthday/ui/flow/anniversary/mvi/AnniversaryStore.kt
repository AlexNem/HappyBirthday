package com.alexnemyr.happybirthday.ui.flow.anniversary.mvi

import com.alexnemyr.happybirthday.ui.common.state.BirthdayState
import com.arkivanov.mvikotlin.core.store.Store

interface AnniversaryStore : Store<AnniversaryStore.Intent, AnniversaryStore.State, AnniversaryStore.Label> {

    sealed class Action {
        data class UpdateUser(val user: BirthdayState) : Action()
    }

    sealed class Intent {
        data object ShowInputScreen : Intent()
        data class Edit(val user: BirthdayState) : Intent()
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
        data class UserData(val result: BirthdayState) : Message()
    }

}