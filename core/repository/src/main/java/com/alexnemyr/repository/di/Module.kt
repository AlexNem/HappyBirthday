package com.alexnemyr.repository.di

import com.alexnemyr.repository.UserRepository
import com.alexnemyr.repository.UserRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<UserRepository> { UserRepositoryImpl(get()) }
}

val storageModule = module {
    includes(com.alexnemyr.storage.di.storageModule)
}