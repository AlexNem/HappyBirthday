package com.alexnemyr.happybirthday.ui.flow.input.mvi

import com.alexnemyr.domain.mapper.toDomain
import com.alexnemyr.domain.view_state.UserState
import com.alexnemyr.mvi.MviExecutor
import com.alexnemyr.usecase.SaveUserUseCase
import com.alexnemyr.usecase.UserFlowUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class InputExecutor(
    private val userFlowUseCase: UserFlowUseCase,
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
            is InputStore.Intent.Edit -> {
                editUser(intent.user)
            }
        }
    }

    private fun setUser(user: UserState) {
        job?.cancel()
        job = scope.launch {
            dispatch(InputStore.Message.Progress)
            delay(1000)
            dispatch(InputStore.Message.UserData(user))
        }
    }

    private fun editUser(state: UserState) {
        job?.cancel()
        job = scope.launch {
            saveUserUseCase.invoke(user = state.toDomain)
            dispatch(InputStore.Message.UserData(state))
        }
    }

}