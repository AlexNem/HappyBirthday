package com.alexnemyr.happybirthday.ui.flow.anniversary.mvi

import com.alexnemyr.domain.mapper.toDomain
import com.alexnemyr.domain.view_state.UserState
import com.alexnemyr.mvi.MviExecutor
import com.alexnemyr.usecase.SaveUserUseCase
import com.alexnemyr.usecase.UserFlowUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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
            is AnniversaryStore.Intent.Edit -> editUser(intent.user)
            is AnniversaryStore.Intent.ShowInputScreen -> navToInput()
        }
    }

    private fun setUser(user: UserState) {
        job?.cancel()
        job = scope.launch {
            dispatch(AnniversaryStore.Message.Progress)
            delay(100)
            dispatch(AnniversaryStore.Message.UserData(user))
        }
    }

    private fun editUser(state: UserState) {
        job?.cancel()
        job = scope.launch {
            saveUserUseCase.invoke(user = state.toDomain)
            dispatch(AnniversaryStore.Message.UserData(state))
        }
    }

    private fun navToInput() {
        publish(AnniversaryStore.Label.NavigateToInput)
    }
}