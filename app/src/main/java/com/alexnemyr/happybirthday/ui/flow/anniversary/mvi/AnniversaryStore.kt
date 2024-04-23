package com.alexnemyr.happybirthday.ui.flow.anniversary.mvi

import com.alexnemyr.domain.domain.UserDomain
import com.alexnemyr.happybirthday.ui.common.state.Error
import com.alexnemyr.happybirthday.ui.flow.anniversary.mvi.AnniversaryStore.Intent
import com.alexnemyr.happybirthday.ui.flow.anniversary.mvi.AnniversaryStore.Label
import com.alexnemyr.happybirthday.ui.flow.anniversary.mvi.AnniversaryStore.State
import com.arkivanov.mvikotlin.core.store.Store

interface AnniversaryStore :
    Store<Intent, State, Label> {

    sealed class Action {
        data class UpdateUser(val user: UserDomain) : Action()
    }

    sealed class Intent {
        data object ShowInputScreen : Intent()
        data object FetchUser : Intent()
        data class EditPicture(val uri: String) : Intent()
    }

    sealed class Label {
        data object NavigateToInput : Label()
    }

    data class State(
        val name: String?,
        val date: String?,
        val uri: String?,
        val isProgress: Boolean = false,
        val error: Error?
    ) {
        companion object {
            val EMPTY_STATE = State(
                name = null,
                date = null,
                uri = null,
                error = null
            )
        }
    }

    sealed class Message {
        data object Progress : Message()
        data class UserData(val result: UserDomain) : Message()
    }

}