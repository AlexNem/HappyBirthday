package com.alexnemyr.happybirthday.ui.flow.input.mvi

import com.alexnemyr.happybirthday.ui.flow.input.mvi.InputStore.Message
import com.alexnemyr.happybirthday.ui.flow.input.mvi.InputStore.State
import com.arkivanov.mvikotlin.core.store.Reducer


class InputReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            is Message.UserData -> copy(
                name = msg.result.name,
                date = msg.result.date,
                uri = msg.result.uri,
                isProgress = false,
            )

            is Message.Progress -> copy(isProgress = true)
            is Message.Error -> copy(
                isProgress = false,
                error = error?.copy(
                    show = true,
                    message = msg.e.message
                )
            )
        }
    }

}