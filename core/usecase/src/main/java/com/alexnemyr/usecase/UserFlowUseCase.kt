package com.alexnemyr.usecase

import com.alexnemyr.repository.UserRepository
import com.alexnemyr.repository.domain.UserDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class UserFlowUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(): Flow<UserDomain> =
        flowOf(userRepository.user)
}