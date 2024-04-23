package com.alexnemyr.happybirthday.ui.flow.input.mvi

import com.alexnemyr.happybirthday.ui.common.state.BirthdayState
import com.arkivanov.mvikotlin.core.store.Reducer

class InputReducer: Reducer<InputStore.State, InputStore.Message> {
    override fun InputStore.State.reduce(msg: InputStore.Message): InputStore.State {
//                return when(msg) {
//            is InputStore.Message.UserData -> copy(name = msg.result.name)
//            is InputStore.Message.Progress -> copy(isProcess = true)
//            is InputStore.Message.Error -> copy(error = msg.e)
//        }
        return when(msg) {
            is InputStore.Message.UserData -> onUserReceived(msg.result)
            is InputStore.Message.Progress -> InputStore.State.Progress
            is InputStore.Message.Error -> onError(msg.e)
        }
    }

    private fun onUserReceived(user: BirthdayState): InputStore.State {
        return InputStore.State.Data(
            user.name,
            user.date,
            user.uriPath
        )
    }

    private fun onError(e: Throwable): InputStore.State = InputStore.State.Error(e)

}