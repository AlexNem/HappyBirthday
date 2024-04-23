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
            is InputStore.Intent.Edit -> editUser(intent.user)
//            is InputStore.Intent.EditName -> editName(intent.name, getState())
            is InputStore.Intent.ShowAnniversaryScreen -> navToAnniversary(getState())
        }
    }

    private fun setUser(user: UserState) {
        job?.cancel()
        job = scope.launch {
            dispatch(InputStore.Message.Progress)
            delay(100)
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
//    private fun editName(name: String, state: InputStore.State) {
//        job?.cancel()
//        job = scope.launch {
//            val userState = UserState(
//                name =
//            )
//            saveUserUseCase.invoke(user = state.copy(name = name).toDomain)
//            dispatch(InputStore.Message.UserData(state))
//        }
//    }

    private fun navToAnniversary(state: InputStore.State) {
        when(state) {
            is InputStore.State.Data -> {
                if (state.date.isNullOrBlank() || state.name.isNullOrBlank()) {
                    dispatch(InputStore.Message.Error(Throwable(message = "Input Name and Date!")))
                } else {
                    publish(InputStore.Label.NavigateToAnniversary)
                }
            }
            else -> {}
        }

//        runCatching {
//            val castedState = state as InputStore.State.Data
//            Timber.tag(TAG).i("navToAnniversary state = $castedState")
//            castedState
//        }.onSuccess {
//            if (it.date.isNullOrBlank() || it.name.isNullOrBlank()) {
//                dispatch(InputStore.Message.Error(Throwable(message = "Input Name and Date!")))
//            } else {
//                publish(InputStore.Label.NavigateToAnniversary)
//            }
//        }.onFailure {
//            dispatch(InputStore.Message.Error(it))
//        }
    }

}