package com.alexnemyr.happybirthday.di

import com.alexnemyr.happybirthday.ui.flow.input.InputViewModel
import com.alexnemyr.happybirthday.ui.flow.anniversary.AnniversaryViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { InputViewModel(get()) }
    viewModel { AnniversaryViewModel(get()) }
}