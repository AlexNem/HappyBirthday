package com.alexnemyr.mvi

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import timber.log.Timber

abstract class MviExecutor<in Intent : Any, in Action : Any, in State : Any, Result : Any, Label : Any> :
    CoroutineExecutor<Intent, Action, State, Result, Label>(
        mainContext = Dispatchers.Main + SupervisorJob() + CoroutineExceptionHandler { _, throwable ->
            if (throwable is CancellationException) {
                throw throwable
            } else {
                Timber.e(throwable)
            }
        }
    )