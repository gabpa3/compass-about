package com.gabcode.compassabout

import android.app.Application
import com.gabcode.compassabout.di.ModuleConfigurator

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        ModuleConfigurator().configure(applicationContext)
    }
}
