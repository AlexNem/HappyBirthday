package com.alexnemyr.happybirthday.ui.flow.input.mvi

import com.alexnemyr.domain.domain.UserDomain
import com.arkivanov.mvikotlin.core.store.Store

interface InputStore : Store<InputStore.Intent, InputStore.State, InputStore.Label> {

    sealed class Action {
        data class UpdateUser(val user: UserDomain) : Action()
    }

    sealed class Intent {
        data object ShowAnniversaryScreen : Intent()
        data class EditName(val name: String) : Intent()
        data class EditDate(val date: String) : Intent()
        data class EditPicture(val uri: String) : Intent()
    }

    sealed class Label {
        data object NavigateToAnniversary : Label()
    }

    data class State(
        val name: String?,
        val date: String?,
        val uri: String?,
        val isProgress: Boolean = false,
        val error: Error?
    ) {
        companion object {
            val nullState = State(
                name = null,
                date = null,
                uri = null,
                error = null
            )
        }
    }

    data class Error(
        val show: Boolean = false,
        val message: String?
    )

    sealed class Message {
        data object Progress : Message()
        data class Error(val e: Throwable) : Message()
        data class UserData(val result: UserDomain) : Message()
    }
}