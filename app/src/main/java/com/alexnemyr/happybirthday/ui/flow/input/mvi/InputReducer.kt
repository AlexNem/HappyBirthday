package com.alexnemyr.happybirthday.ui.flow.input.mvi

import com.arkivanov.mvikotlin.core.store.Reducer

class InputReducer : Reducer<InputStore.State, InputStore.Message> {
    override fun InputStore.State.reduce(msg: InputStore.Message): InputStore.State {
        return when (msg) {
            is InputStore.Message.UserData -> copy(
                name = msg.result.name,
                date = msg.result.date,
                uri = msg.result.uri,
                isProgress = false,
            )

            is InputStore.Message.Progress -> copy(isProgress = true)
            is InputStore.Message.Error -> copy(
                isProgress = false,
                error = InputStore.Error(
                    show = true,
                    message = msg.e.message
                )
            )
        }
    }

}