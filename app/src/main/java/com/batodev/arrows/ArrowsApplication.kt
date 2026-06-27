package com.batodev.arrows

import android.app.Application
import com.batodev.arrows.data.di.dataModule
import com.batodev.arrows.ui.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ArrowsApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ArrowsApplication)
            modules(dataModule, viewModelModule)
        }
    }
}
