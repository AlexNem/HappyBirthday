package com.alexnemyr.happybirthday

import android.app.Application
import com.alexnemyr.happybirthday.di.viewModelModule
import com.alexnemyr.repository.di.repositoryModule
import com.alexnemyr.repository.di.storageModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initTimber()
        initDI()
    }


    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun initDI() {
        startKoin {
            androidContext(this@App)
            modules(
                listOf(
                    viewModelModule,
                    repositoryModule,
                    storageModule
                )
            )
        }
    }
}

const val TAG = "TEST_TAG"