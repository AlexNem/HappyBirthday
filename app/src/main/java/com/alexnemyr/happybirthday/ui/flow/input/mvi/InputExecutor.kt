package com.alexnemyr.happybirthday.ui.flow.input.mvi

import com.alexnemyr.domain.domain.UserDomain
import com.alexnemyr.happybirthday.ui.flow.input.mvi.InputStore.Action
import com.alexnemyr.happybirthday.ui.flow.input.mvi.InputStore.Intent
import com.alexnemyr.happybirthday.ui.flow.input.mvi.InputStore.Label
import com.alexnemyr.happybirthday.ui.flow.input.mvi.InputStore.Message
import com.alexnemyr.happybirthday.ui.flow.input.mvi.InputStore.State
import com.alexnemyr.mvi.MviExecutor
import com.alexnemyr.usecase.SaveUserUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class InputExecutor(
    private val saveUserUseCase: SaveUserUseCase
) : MviExecutor<Intent, Action, State, Message, Label>() {

    private var job: Job? = null

    override fun executeAction(action: Action, getState: () -> State) {
        when (action) {
            is Action.UpdateUser -> setUser(action.user)
        }
    }

    override fun executeIntent(intent: Intent, getState: () -> State) {
        when (intent) {
            is Intent.EditName -> editName(name = intent.name, state = getState())
            is Intent.EditDate -> editDate(date = intent.date, state = getState())
            is Intent.EditPicture -> editUri(uri = intent.uri, state = getState())
            is Intent.ShowAnniversaryScreen -> navToAnniversary(getState())
        }
    }

    private fun setUser(user: UserDomain) {
        job?.cancel()
        job = scope.launch {
            dispatch(Message.Progress)
            delay(1000)
            dispatch(Message.UserData(user))
        }
    }

    private fun editName(name: String, state: State) {
        job?.cancel()
        job = scope.launch {
            val newState = state.copy(name = name)
            saveUserUseCase.invoke(user = newState.toDomain)
            dispatch(Message.UserData(newState.toDomain))
        }
    }

    private fun editDate(date: String, state: State) {
        job?.cancel()
        job = scope.launch {
            val newState = state.copy(date = date)
            saveUserUseCase.invoke(user = newState.toDomain)
            dispatch(Message.UserData(newState.toDomain))
        }
    }

    private fun editUri(uri: String, state: State) {
        job?.cancel()
        job = scope.launch {
            val newState = state.copy(uri = uri).toDomain
            saveUserUseCase.invoke(user = newState)
            dispatch(Message.UserData(newState))
        }
    }

    private fun navToAnniversary(state: State) {
        if (state.date.isNullOrBlank() || state.name.isNullOrBlank()) {
            dispatch(Message.Error(Throwable(message = "Input Name and Date!")))
        } else {
            publish(Label.NavigateToAnniversary)
        }
    }
}

val State.toDomain: UserDomain
    get() = UserDomain(
        name = this.name,
        date = this.date,
        uri = this.uri
    )