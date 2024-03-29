package com.alexnemyr.repository

import com.alexnemyr.repository.domain.UserDomain

interface UserRepository {
    val user: UserDomain
    fun saveUser(user: UserDomain)
}