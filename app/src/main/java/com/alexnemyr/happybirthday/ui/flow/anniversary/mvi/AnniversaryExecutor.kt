package com.alexnemyr.happybirthday.ui.flow.anniversary.mvi

import com.alexnemyr.domain.domain.UserDomain
import com.alexnemyr.happybirthday.ui.flow.anniversary.mvi.AnniversaryStore.Action
import com.alexnemyr.happybirthday.ui.flow.anniversary.mvi.AnniversaryStore.Intent
import com.alexnemyr.happybirthday.ui.flow.anniversary.mvi.AnniversaryStore.Label
import com.alexnemyr.happybirthday.ui.flow.anniversary.mvi.AnniversaryStore.Message
import com.alexnemyr.happybirthday.ui.flow.anniversary.mvi.AnniversaryStore.State
import com.alexnemyr.mvi.MviExecutor
import com.alexnemyr.usecase.SaveUserUseCase
import com.alexnemyr.usecase.UserFlowUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class AnniversaryExecutor(
    private val userFlowUseCase: UserFlowUseCase,
    private val saveUserUseCase: SaveUserUseCase
) : MviExecutor<Intent, Action, State, Message, Label>() {

    private var job: Job? = null

    override fun executeAction(
        action: Action,
        getState: () -> State
    ) {
        when (action) {
            is Action.UpdateUser -> setUser(action.user)
        }
    }

    override fun executeIntent(
        intent: Intent,
        getState: () -> State
    ) {
        when (intent) {
            is Intent.FetchUser -> fetchUser()
            is Intent.EditPicture -> editPicture(intent.uri, getState())
            is Intent.ShowInputScreen -> navToInput()
        }
    }

    private fun fetchUser() {
        job?.cancel()
        job = scope.launch {
            userFlowUseCase.invoke()
                .onEach { dispatch(Message.UserData(it)) }
                .launchIn(this)
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

    private fun editPicture(uri: String, state: State) {
        job?.cancel()
        job = scope.launch {
            val newState = state.copy(uri = uri)
            saveUserUseCase.invoke(user = newState.toDomain)
            dispatch(Message.UserData(newState.toDomain))
        }
    }

    private fun navToInput() {
        publish(Label.NavigateToInput)
    }
}

val State.toDomain: UserDomain
    get() = UserDomain(
        name = this.name,
        date = this.date,
        uri = this.uri
    )