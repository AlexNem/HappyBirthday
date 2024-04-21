package com.alexnemyr.usecase.di

import com.alexnemyr.usecase.UserFlowUseCase
import org.koin.dsl.module

val useCaseModule = module {
    single { UserFlowUseCase(get()) }
}