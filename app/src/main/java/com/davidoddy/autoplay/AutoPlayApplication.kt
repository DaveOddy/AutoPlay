package com.davidoddy.autoplay

import android.app.Application
import timber.log.Timber

/**
 * Created by doddy on 12/9/17.
 */
class AutoPlayApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
    }

}