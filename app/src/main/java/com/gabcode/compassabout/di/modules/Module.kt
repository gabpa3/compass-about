package com.gabcode.compassabout.di.modules

import com.gabcode.compassabout.di.ServiceLocator

interface Module {
    fun load(serviceLocator: ServiceLocator)
}
