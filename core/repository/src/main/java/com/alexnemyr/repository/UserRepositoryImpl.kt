package com.alexnemyr.repository

import com.alexnemyr.repository.domain.UserDomain
import com.alexnemyr.storage.AppPreferences

class UserRepositoryImpl(
    private val preferences: AppPreferences
) : UserRepository {
    override val test: String
        get() = preferences.name ?: ""
    override val user: UserDomain
        get() = UserDomain(
            preferences.name,
            preferences.date,
            preferences.uri
        )

    override fun saveUser(user: UserDomain) {
        user.apply {
            name?.let { preferences.putName(it) }
            date?.let { preferences.putDate(it) }
            uri?.let { preferences.putUri(it) }
        }
    }
}