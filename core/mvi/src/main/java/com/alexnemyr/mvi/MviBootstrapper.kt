package com.alexnemyr.mvi

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import timber.log.Timber


abstract class MviBootstrapper<Action : Any> : CoroutineBootstrapper<Action>(
    mainContext = Dispatchers.Main + SupervisorJob() + CoroutineExceptionHandler { _, throwable ->
        if (throwable is CancellationException) {
            throw throwable
        } else {
            Timber.e(throwable)
        }
    }
)