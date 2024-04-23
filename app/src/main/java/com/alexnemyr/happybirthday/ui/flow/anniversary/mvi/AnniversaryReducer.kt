package com.alexnemyr.happybirthday.ui.flow.anniversary.mvi

import com.alexnemyr.domain.view_state.UserState
import com.arkivanov.mvikotlin.core.store.Reducer

class AnniversaryReducer : Reducer<AnniversaryStore.State, AnniversaryStore.Message> {
    override fun AnniversaryStore.State.reduce(msg: AnniversaryStore.Message): AnniversaryStore.State {
        return when (msg) {
            is AnniversaryStore.Message.Progress -> AnniversaryStore.State.Progress
            is AnniversaryStore.Message.UserData -> onUserReceived(msg.result)
        }
    }

    private fun onUserReceived(user: UserState): AnniversaryStore.State {
        return AnniversaryStore.State.Data(
            user.name,
            user.date,
            user.uriPath
        )
    }
}
