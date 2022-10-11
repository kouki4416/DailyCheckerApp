package com.pyunku.dailychecker.app

import android.app.Application
import org.koin.core.context.startKoin

class DailyCheckerApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin{
            modules(applicationModule)
        }
    }
}