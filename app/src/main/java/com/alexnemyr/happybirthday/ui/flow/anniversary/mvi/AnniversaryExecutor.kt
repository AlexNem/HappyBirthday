package com.alexnemyr.happybirthday.ui.flow.anniversary.mvi

import com.alexnemyr.happybirthday.ui.common.state.BirthdayState
import com.alexnemyr.happybirthday.ui.common.state.toDomain
import com.alexnemyr.happybirthday.ui.common.state.toViewState
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
) : MviExecutor<AnniversaryStore.Intent, AnniversaryStore.Action, AnniversaryStore.State, AnniversaryStore.Message, AnniversaryStore.Label>() {

    private var job: Job? = null

    override fun executeAction(
        action: AnniversaryStore.Action,
        getState: () -> AnniversaryStore.State
    ) {
        when (action) {
            is AnniversaryStore.Action.UpdateUser -> setUser(action.user)
        }
    }

    override fun executeIntent(
        intent: AnniversaryStore.Intent,
        getState: () -> AnniversaryStore.State
    ) {
        when (intent) {
            is AnniversaryStore.Intent.FetchUser -> fetchUser()
            is AnniversaryStore.Intent.EditPicture -> editPicture(intent.uri, getState())
            is AnniversaryStore.Intent.ShowInputScreen -> navToInput()
        }
    }

    private fun fetchUser() {
        job?.cancel()
        job = scope.launch {
            userFlowUseCase.invoke()
                .onEach { dispatch(AnniversaryStore.Message.UserData(it.toViewState)) }
                .launchIn(this)
        }
    }

    private fun setUser(user: BirthdayState) {
        job?.cancel()
        job = scope.launch {
            dispatch(AnniversaryStore.Message.Progress)
            delay(1000)
            dispatch(AnniversaryStore.Message.UserData(user))
        }
    }

    private fun editPicture(uri: String, state: AnniversaryStore.State) {
        job?.cancel()
        job = scope.launch {
            val userState = BirthdayState(
                name = state.name,
                date = state.date,
                uriPath = uri
            )
            saveUserUseCase.invoke(user = userState.toDomain)
            dispatch(AnniversaryStore.Message.UserData(userState))
        }
    }

    private fun navToInput() {
        publish(AnniversaryStore.Label.NavigateToInput)
    }
}