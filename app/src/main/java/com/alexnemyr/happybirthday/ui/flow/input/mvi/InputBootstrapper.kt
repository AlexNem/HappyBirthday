package com.alexnemyr.happybirthday.ui.flow.input.mvi

import com.alexnemyr.domain.util.TAG
import com.alexnemyr.happybirthday.ui.common.state.toViewState
import com.alexnemyr.mvi.MviBootstrapper
import com.alexnemyr.usecase.UserFlowUseCase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber

class InputBootstrapper(
    private val userFlowUseCase: UserFlowUseCase
) : MviBootstrapper<InputStore.Action>() {
    override fun invoke() {
        scope.launch {
            userFlowUseCase.invoke()
                .onEach {
                    Timber.tag(TAG).d("InputBootstrapper -> onEach = $it")
                    dispatch(InputStore.Action.UpdateUser(it.toViewState))
                }
                .launchIn(this)
        }
    }
}