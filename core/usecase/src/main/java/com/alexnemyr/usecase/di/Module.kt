package com.alexnemyr.usecase.di

import com.alexnemyr.usecase.SaveUserUseCase
import com.alexnemyr.usecase.UserFlowUseCase
import org.koin.dsl.module

val useCaseModule = module {
    single { UserFlowUseCase(get()) }
    single { SaveUserUseCase(get()) }
}

val repositoryModule = module {
    includes(com.alexnemyr.repository.di.repositoryModule)
}

val storageModule = module {
    includes(com.alexnemyr.repository.di.storageModule)
}
