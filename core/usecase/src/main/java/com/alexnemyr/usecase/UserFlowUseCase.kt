package com.alexnemyr.usecase

import com.alexnemyr.domain.domain.UserDomain
import com.alexnemyr.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class UserFlowUseCase(private val userRepository: UserRepository) {
    operator fun invoke(): Flow<UserDomain> {
        return flowOf(userRepository.user)
    }

}