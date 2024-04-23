package com.alexnemyr.happybirthday.ui.flow.anniversary.mvi

import com.arkivanov.mvikotlin.core.store.Reducer

class AnniversaryReducer : Reducer<AnniversaryStore.State, AnniversaryStore.Message> {
    override fun AnniversaryStore.State.reduce(msg: AnniversaryStore.Message): AnniversaryStore.State {
        return when (msg) {
            is AnniversaryStore.Message.Progress -> copy(isProgress = true)
            is AnniversaryStore.Message.UserData ->
                copy(
                    name = msg.result.name,
                    date = msg.result.date,
                    uri = msg.result.uri,
                    isProgress = false
                )
        }
    }

}
