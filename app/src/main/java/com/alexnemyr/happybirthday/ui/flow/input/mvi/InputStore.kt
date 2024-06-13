package com.alexnemyr.happybirthday.ui.flow.input.mvi

import com.alexnemyr.domain.domain.UserDomain
import com.alexnemyr.happybirthday.ui.common.state.Error
import com.alexnemyr.happybirthday.ui.flow.input.mvi.InputStore.Intent
import com.alexnemyr.happybirthday.ui.flow.input.mvi.InputStore.Label
import com.alexnemyr.happybirthday.ui.flow.input.mvi.InputStore.State
import com.arkivanov.mvikotlin.core.store.Store

interface InputStore : Store<Intent, State, Label> {

    sealed class Action {
        data class UpdateUser(val user: UserDomain) : Action()
    }

    sealed class Intent {
        data object OnAnniversaryScreen : Intent()
        data class OnDatePicker(val show: Boolean) : Intent()
        data class OnPicturePicker(val show: Boolean) : Intent()
        data class EditName(val name: String) : Intent()
        data class EditDate(val date: String) : Intent()
        data class EditPicture(val uri: String) : Intent()
    }

    sealed class Label {
        data object NavigateToAnniversary : Label()
    }

    data class State(
        val name: String? = null,
        val date: String? = null,
        val uri: String? = null,
        val isProgress: Boolean = false,
        val error: Error? = null,
        val showDatePicker: Boolean = false,
        val showPicturePicker: Boolean = false
    )

    sealed class Message {
        data object Progress : Message()
        data class Error(val e: Throwable) : Message()
        data class UserData(val result: State) : Message()
        data class ShowPicturePicker(val value: Boolean) : Message()
        data class ShowDatePicker(val value: Boolean) : Message()
    }
}
