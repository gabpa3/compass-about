package com.gabcode.compassabout.di.modules

import com.gabcode.compassabout.data.AboutRepository
import com.gabcode.compassabout.data.IAboutRepository
import com.gabcode.compassabout.data.IAboutService
import com.gabcode.compassabout.data.NetworkClient
import com.gabcode.compassabout.data.database.AppDatabase
import com.gabcode.compassabout.di.ServiceLocator
import com.gabcode.compassabout.di.ServiceLocator.get
import kotlinx.coroutines.Dispatchers

class DataModule : Module {

    override fun load(serviceLocator: ServiceLocator) {
        ServiceLocator.register(AppDatabase::class.java, AppDatabase.getInstance(get()))
        ServiceLocator.register(IAboutService::class.java, NetworkClient())
        ServiceLocator.register(
            IAboutRepository::class.java,
            AboutRepository(get(), get<AppDatabase>().aboutDao(), Dispatchers.IO)
        )
    }
}
