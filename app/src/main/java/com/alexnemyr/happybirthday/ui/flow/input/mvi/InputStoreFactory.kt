package com.alexnemyr.happybirthday.ui.flow.input.mvi

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory

class InputStoreFactory(
    private val storeFactory: StoreFactory,
    private val bootstrapper: InputBootstrapper,
    private val executor: InputExecutor,
    private val reducer: InputReducer,
) {
    fun create(): InputStore {
        return object : InputStore, Store<InputStore.Intent, InputStore.State, InputStore.Label> by storeFactory.create(
            name = this.javaClass.simpleName,
            initialState = InputStore.State.Progress,
            bootstrapper = bootstrapper,
            executorFactory = ::getFactory,
            reducer = reducer
        ) {}
    }

    private fun getFactory(): InputExecutor {
        return executor
    }
}