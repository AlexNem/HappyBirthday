package com.alexnemyr.mvi

import androidx.lifecycle.ViewModel
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

abstract class MviViewModel<in Intent : Any, out State : Any, out Label : Any, S : Store<Intent, State, Label>>(
    private val store: S
) : ViewModel() {

    /**
     * Why this flow is not a StateFlow?
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val states: StateFlow<State>
        get() = store.stateFlow

    val state: State
        get() = store.state

    val labels: Flow<Label>
        get() = store.labels

    fun accept(intent: Intent) {
        store.accept(intent)
    }

    override fun onCleared() {
        super.onCleared()
        store.dispose()
    }
}
