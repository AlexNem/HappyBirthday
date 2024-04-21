package com.alexnemyr.happybirthday.ui.common

import android.net.Uri
import com.alexnemyr.happybirthday.TAG
import com.alexnemyr.repository.domain.UserDomain
import timber.log.Timber

data class UserState(
    val name: String?,
    val date: String?,
    val uriPath: String?,
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

val UserDomain.toViewState: UserState
    get() {
        return UserState(
            this.name,
            this.date,
            this.uri
        )
    }

val UserState.toDomain: UserDomain
    get() {
        return UserDomain(
            this.name,
            this.date,
            this.uriPath
        )
    }