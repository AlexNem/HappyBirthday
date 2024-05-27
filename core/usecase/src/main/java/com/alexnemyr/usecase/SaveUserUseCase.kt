package com.alexnemyr.usecase

import com.alexnemyr.domain.domain.UserDomain
import com.alexnemyr.repository.UserRepository

class SaveUserUseCase(
    private val userRepository: UserRepository
) {
    suspend fun invoke(user: UserDomain) {
        userRepository.saveUser(user)
    }
}