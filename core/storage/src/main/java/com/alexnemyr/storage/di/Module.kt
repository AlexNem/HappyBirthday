package com.alexnemyr.storage.di

import com.alexnemyr.storage.AppPreferences
import com.alexnemyr.storage.AppPreferencesImpl
import org.koin.dsl.module

val storageModule = module {
    single<AppPreferences> { AppPreferencesImpl(get()) }
}