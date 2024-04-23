package com.alexnemyr.happybirthday.ui.flow.input.mvi

import com.alexnemyr.happybirthday.ui.flow.input.mvi.InputStore.Action
import com.alexnemyr.mvi.MviBootstrapper
import com.alexnemyr.usecase.UserFlowUseCase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class InputBootstrapper(
    private val userFlowUseCase: UserFlowUseCase
) : MviBootstrapper<Action>() {
    override fun invoke() {
        scope.launch {
            userFlowUseCase.invoke()
                .onEach { dispatch(Action.UpdateUser(it)) }
                .launchIn(this)
        }
    }
}