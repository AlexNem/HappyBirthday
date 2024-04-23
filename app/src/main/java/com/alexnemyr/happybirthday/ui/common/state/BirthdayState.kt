package com.alexnemyr.happybirthday.ui.common.state

import com.alexnemyr.domain.domain.UserDomain
import com.alexnemyr.domain.util.age

data class BirthdayState(
    val name: String?,
    val date: String?,
    val uriPath: String?,
)

val UserDomain.toViewState: BirthdayState
    get() {
        return BirthdayState(
            this.name,
            this.date,
            this.uri
        )
    }

val BirthdayState.toDomain: UserDomain
    get() {
        return UserDomain(
            this.name,
            this.date,
            this.uriPath,
            this.date?.age
        )
    }

