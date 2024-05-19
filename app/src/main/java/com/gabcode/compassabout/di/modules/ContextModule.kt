package com.gabcode.compassabout.di.modules

import android.content.Context
import com.gabcode.compassabout.di.ServiceLocator

internal class ContextModule(private val context: Context) : Module {
    override fun load(serviceLocator: ServiceLocator) {
        ServiceLocator.register(Context::class.java, context)
    }
}
