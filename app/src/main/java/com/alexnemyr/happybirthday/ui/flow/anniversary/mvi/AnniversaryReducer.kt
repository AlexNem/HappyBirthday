package com.alexnemyr.happybirthday.ui.flow.anniversary.mvi

import com.alexnemyr.happybirthday.ui.flow.anniversary.mvi.AnniversaryStore.Message
import com.alexnemyr.happybirthday.ui.flow.anniversary.mvi.AnniversaryStore.State
import com.arkivanov.mvikotlin.core.store.Reducer

class AnniversaryReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            is Message.Progress -> copy(isProgress = true)
            is Message.UserData ->
                copy(
                    name = msg.result.name,
                    date = msg.result.date,
                    uri = msg.result.uri,
                    isProgress = false
                )
            is Message.ShowPicturePicker -> copy(showPicturePicker = msg.value)
        }
    }

}
