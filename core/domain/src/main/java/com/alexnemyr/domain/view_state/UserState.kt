package com.alexnemyr.domain.view_state

import android.net.Uri
import com.alexnemyr.domain.util.TAG
import timber.log.Timber

//todo: move to UI layer
data class UserState(
    val name: String?,
    val date: String?,
    val uriPath: String?,
)

data class Name(
    val name: String?,
    val isError: Boolean?
)

val String.toUriOrEmpty: Uri
    get() = runCatching {
        try {
            Uri.parse(this)
        } catch (e: Exception) {
            Timber.tag(TAG).e("toUriOrEmpty -> ${e.message}")
            throw Exception()
        }
    }.getOrElse { Uri.EMPTY }

