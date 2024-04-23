package com.alexnemyr.happybirthday.ui.flow.anniversary.mvi

import com.alexnemyr.domain.mapper.toViewState
import com.alexnemyr.mvi.MviBootstrapper
import com.alexnemyr.usecase.UserFlowUseCase
import kotlinx.coroutines.launch

class AnniversaryBootstrapper(
    private val userFlowUseCase: UserFlowUseCase
) : MviBootstrapper<AnniversaryStore.Action>() {
    override fun invoke() {
        scope.launch {
            userFlowUseCase.invoke()
                .collect {
                    dispatch(AnniversaryStore.Action.UpdateUser(it.toViewState))
                }
        }
    }
}