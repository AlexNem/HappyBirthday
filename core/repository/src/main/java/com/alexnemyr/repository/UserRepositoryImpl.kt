package com.alexnemyr.repository

import com.alexnemyr.domain.domain.UserDomain
import com.alexnemyr.domain.util.age
import com.alexnemyr.storage.AppPreferences

class UserRepositoryImpl(
    private val preferences: AppPreferences
) : UserRepository {
    override val user: UserDomain
        get() = UserDomain(
            preferences.name,
            preferences.date,
            preferences.uri,
            preferences.date?.age
        )

    override fun saveUser(user: UserDomain) {
        user.apply {
            name?.let { preferences.putName(it) }
            date?.let { preferences.putDate(it) }
            uri?.let { preferences.putUri(it) }
        }
    }
}