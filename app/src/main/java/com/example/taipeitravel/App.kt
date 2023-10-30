package com.example.taipeitravel

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    companion object {
        private lateinit var _instance: App
        var instance: App
            get() = _instance
            set(value) {}
    }

    override fun onCreate() {
        super.onCreate()
        _instance = this
    }
}
