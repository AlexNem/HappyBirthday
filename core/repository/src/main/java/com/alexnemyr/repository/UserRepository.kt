package com.alexnemyr.repository

import com.alexnemyr.domain.domain.UserDomain

interface UserRepository {
    val user: UserDomain
    fun saveUser(user: UserDomain)
}