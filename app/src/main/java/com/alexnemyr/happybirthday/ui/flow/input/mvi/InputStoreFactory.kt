package com.alexnemyr.happybirthday.ui.flow.input.mvi

import com.alexnemyr.happybirthday.ui.flow.input.mvi.InputStore.Intent
import com.alexnemyr.happybirthday.ui.flow.input.mvi.InputStore.Label
import com.alexnemyr.happybirthday.ui.flow.input.mvi.InputStore.State
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory

class InputStoreFactory(
    private val storeFactory: StoreFactory,
    private val bootstrapper: InputBootstrapper,
    private val executor: InputExecutor,
    private val reducer: InputReducer,
) {
    fun create(): InputStore {
        return object : InputStore,
            Store<Intent, State, Label> by storeFactory.create(
                name = this.javaClass.simpleName,
                initialState = State.EMPTY_STATE,
                bootstrapper = bootstrapper,
                executorFactory = ::getFactory,
                reducer = reducer
            ) {}
    }

    private fun getFactory(): InputExecutor {
        return executor
    }
}