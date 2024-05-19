package com.gabcode.compassabout.di

import android.content.Context
import com.gabcode.compassabout.di.modules.ContextModule
import com.gabcode.compassabout.di.modules.DataModule
import com.gabcode.compassabout.di.modules.DomainModule
import com.gabcode.compassabout.di.modules.Module

class ModuleConfigurator {

    fun configure(context: Context) {
        register(
            ContextModule(context),
            DataModule(),
            DomainModule()
        )
    }

    private fun register(vararg modules: Module) {
        ServiceLocator.removeAll()
        modules.forEach { module -> module.load(ServiceLocator) }
    }
}
