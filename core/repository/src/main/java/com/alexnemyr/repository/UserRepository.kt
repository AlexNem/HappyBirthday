package com.alexnemyr.repository

import com.alexnemyr.repository.domain.UserDomain

interface UserRepository {
    val test: String
    val user: UserDomain
    fun saveUser(user: UserDomain)
}