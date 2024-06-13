package com.alexnemyr.happybirthday.ui.flow.input.mvi

import com.alexnemyr.happybirthday.ui.common.MVI_TAG
import com.alexnemyr.happybirthday.ui.flow.input.mvi.InputStore.Message
import com.alexnemyr.happybirthday.ui.flow.input.mvi.InputStore.State
import com.arkivanov.mvikotlin.core.store.Reducer
import timber.log.Timber


class InputReducer : Reducer<State, Message> {

    override fun State.reduce(msg: Message): State {
        Timber.tag(MVI_TAG).d("reduce -> msg = $msg")
        return when (msg) {
            is Message.UserData -> copy(
                name = msg.result.name,
                date = msg.result.date,
                uri = msg.result.uri
            )

            is Message.Progress -> copy(isProgress = true)
            is Message.Error -> copy(
                isProgress = false,
                error = error?.copy(
                    show = true,
                    message = msg.e.message
                )
            )

            is Message.ShowDatePicker -> copy(showDatePicker = msg.value)
            is Message.ShowPicturePicker -> copy(showPicturePicker = msg.value)
        }
    }

}
