package com.alexnemyr.happybirthday.di

import com.alexnemyr.happybirthday.ui.input.InputViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { InputViewModel() }
}