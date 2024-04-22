package com.alexnemyr.happybirthday.ui.flow.input.mvi

import com.alexnemyr.domain.view_state.UserState
import com.arkivanov.mvikotlin.core.store.Reducer

class InputReducer: Reducer<InputStore.State, InputStore.Message> {
    override fun InputStore.State.reduce(msg: InputStore.Message): InputStore.State {
        return when(msg) {
            is InputStore.Message.Progress -> InputStore.State.Progress
            is InputStore.Message.UserData -> onUserReceived(msg.result)
        }
    }

    private fun onUserReceived(user: UserState): InputStore.State {
        return InputStore.State.Data(
            user.name,
            user.date,
            user.uriPath
        )
    }

}