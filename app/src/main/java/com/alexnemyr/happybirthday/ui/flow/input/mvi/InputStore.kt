package com.alexnemyr.happybirthday.ui.flow.input.mvi

import com.alexnemyr.domain.view_state.UserState
import com.arkivanov.mvikotlin.core.store.Store

interface InputStore : Store<InputStore.Intent, InputStore.State, InputStore.Label> {

    sealed class Action {
        data class UpdateUser(val user: UserState): Action()
    }

    sealed class Intent {
        data object ShowAnniversaryScreen: Intent()
        data class Edit(val user: UserState) : Intent()
//        data class EditName(val name: String) : Intent()
    }

    sealed class Label {
        data object NavigateToAnniversary : Label()
    }

    sealed class State {
        data object Progress : State()//useless
        data class Error(val message: Throwable) : State()//useless
//        //todo: only one data class
        data class Data(
            var name: String?,
            var date: String?,
            var uri: String?
        ) : State()
    }

    //for refactoring
//    data class State(
//        var name: String?,
//        var date: String?,
//        var uri: String?,
//        val error: Throwable?,
//        val isProcess: Boolean = false
//    )
    data class EditText(
        val value: String,
        val isError: Boolean,
        val color: Int,

    )

    sealed class Message {
        data object Progress : Message()
        data class Error(val e: Throwable) : Message()
        data class UserData(val result: UserState): Message()
    }
}