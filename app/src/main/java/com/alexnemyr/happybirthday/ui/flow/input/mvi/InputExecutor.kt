package com.alexnemyr.happybirthday.ui.flow.input.mvi

import com.alexnemyr.happybirthday.ui.common.state.BirthdayState
import com.alexnemyr.happybirthday.ui.common.state.toDomain
import com.alexnemyr.mvi.MviExecutor
import com.alexnemyr.usecase.SaveUserUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class InputExecutor(
    private val saveUserUseCase: SaveUserUseCase
) : MviExecutor<InputStore.Intent, InputStore.Action, InputStore.State, InputStore.Message, InputStore.Label>() {

    private var job: Job? = null

    override fun executeAction(action: InputStore.Action, getState: () -> InputStore.State) {
        when (action) {
            is InputStore.Action.UpdateUser -> setUser(action.user)
        }
    }

    override fun executeIntent(intent: InputStore.Intent, getState: () -> InputStore.State) {
        when (intent) {
            is InputStore.Intent.EditName -> editName(name = intent.name, state = getState())
            is InputStore.Intent.EditDate -> editDate(date = intent.date, state = getState())
            is InputStore.Intent.EditPicture -> editUri(uri = intent.uri, state = getState())
            is InputStore.Intent.ShowAnniversaryScreen -> navToAnniversary(getState())
        }
    }

    private fun setUser(user: BirthdayState) {
        job?.cancel()
        job = scope.launch {
            dispatch(InputStore.Message.Progress)
            delay(1000)
            dispatch(InputStore.Message.UserData(user))
        }
    }

    private fun editName(name: String, state: InputStore.State) {
        job?.cancel()
        job = scope.launch {
            val userState = BirthdayState(
                name = name,
                date = state.date,
                uriPath = state.uri
            )
            saveUserUseCase.invoke(user = userState.toDomain)
            dispatch(InputStore.Message.UserData(userState))
        }
    }

    private fun editDate(date: String, state: InputStore.State) {
        job?.cancel()
        job = scope.launch {
            val userState = BirthdayState(
                name = state.name,
                date = date,
                uriPath = state.uri
            )
            saveUserUseCase.invoke(user = userState.toDomain)
            dispatch(InputStore.Message.UserData(userState))
        }
    }

    private fun editUri(uri: String, state: InputStore.State) {
        job?.cancel()
        job = scope.launch {
            val userState = BirthdayState(
                name = state.name,
                date = state.date,
                uriPath = uri
            )
            saveUserUseCase.invoke(user = userState.toDomain)
            dispatch(InputStore.Message.UserData(userState))
        }
    }

    private fun navToAnniversary(state: InputStore.State) {
        if (state.date.isNullOrBlank() || state.name.isNullOrBlank()) {
            dispatch(InputStore.Message.Error(Throwable(message = "Input Name and Date!")))
        } else {
            publish(InputStore.Label.NavigateToAnniversary)
        }
    }
}