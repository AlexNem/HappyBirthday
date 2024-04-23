package com.alexnemyr.happybirthday.ui.flow.anniversary.mvi

import com.alexnemyr.happybirthday.ui.flow.anniversary.mvi.AnniversaryStore.Intent
import com.alexnemyr.happybirthday.ui.flow.anniversary.mvi.AnniversaryStore.Label
import com.alexnemyr.happybirthday.ui.flow.anniversary.mvi.AnniversaryStore.State
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory

class AnniversaryStoreFactory(
    private val storeFactory: StoreFactory,
    private val bootstrapper: AnniversaryBootstrapper,
    private val executor: AnniversaryExecutor,
    private val reducer: AnniversaryReducer,
) {
    fun create(): AnniversaryStore {
        return object : AnniversaryStore,
            Store<Intent, State, Label> by storeFactory.create(
                name = this.javaClass.simpleName,
                initialState = State.EMPTY_STATE,
                bootstrapper = bootstrapper,
                executorFactory = ::getFactory,
                reducer = reducer
            ) {}
    }

    private fun getFactory(): AnniversaryExecutor {
        return executor
    }
}