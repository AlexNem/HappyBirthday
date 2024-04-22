package com.alexnemyr.domain.mapper

import com.alexnemyr.domain.domain.UserDomain
import com.alexnemyr.domain.view_state.UserState

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