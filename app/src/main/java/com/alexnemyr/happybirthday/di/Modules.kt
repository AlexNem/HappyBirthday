package com.alexnemyr.happybirthday.di

import com.alexnemyr.happybirthday.BuildConfig
import com.alexnemyr.happybirthday.ui.flow.anniversary.AnniversaryViewModel
import com.alexnemyr.happybirthday.ui.flow.anniversary.mvi.AnniversaryBootstrapper
import com.alexnemyr.happybirthday.ui.flow.anniversary.mvi.AnniversaryExecutor
import com.alexnemyr.happybirthday.ui.flow.anniversary.mvi.AnniversaryReducer
import com.alexnemyr.happybirthday.ui.flow.anniversary.mvi.AnniversaryStoreFactory
import com.alexnemyr.happybirthday.ui.flow.input.InputViewModel
import com.alexnemyr.happybirthday.ui.flow.input.mvi.InputBootstrapper
import com.alexnemyr.happybirthday.ui.flow.input.mvi.InputExecutor
import com.alexnemyr.happybirthday.ui.flow.input.mvi.InputReducer
import com.alexnemyr.happybirthday.ui.flow.input.mvi.InputStoreFactory
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.logging.logger.DefaultLogFormatter
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { AnniversaryViewModel(get()) }
    viewModel { InputViewModel(get()) }
}

val mviModule = module {
    factory<StoreFactory> {
        if (BuildConfig.DEBUG) LoggingStoreFactory(
            delegate = DefaultStoreFactory(),
            logFormatter = DefaultLogFormatter()
        )
        else DefaultStoreFactory()

    }
    //Input
    single { InputBootstrapper(get()) }
    single { InputExecutor(get()) }
    single { InputReducer() }
    single { InputStoreFactory(get(), get(), get(), get()) }
    //Anniversary
    single { AnniversaryBootstrapper(get()) }
    single { AnniversaryExecutor(get()) }
    single { AnniversaryReducer() }
    single { AnniversaryStoreFactory(get(), get(), get(), get()) }
}