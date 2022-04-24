package com.example.mydish

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class FavDishApplication : Application() {
    override fun onCreate() {
        super.onCreate()
     //   Timber.plant(Timber.DebugTree())
    }
}