package com.alexnemyr.happybirthday.ui.flow.input.mvi

import com.alexnemyr.happybirthday.ui.common.toViewState
import com.alexnemyr.mvi.MviBootstrapper
import com.alexnemyr.usecase.UserFlowUseCase
import kotlinx.coroutines.launch

class InputBootstrapper(
    private val userFlowUseCase: UserFlowUseCase
) : MviBootstrapper<InputStore.Action>() {
    override fun invoke() {
        scope.launch {
            userFlowUseCase.invoke()
                .collect {
                    dispatch(InputStore.Action.UpdateUser(it.toViewState))
                }
        }
    }
}