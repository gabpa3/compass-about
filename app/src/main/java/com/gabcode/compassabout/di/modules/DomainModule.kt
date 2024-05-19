package com.gabcode.compassabout.di.modules

import com.gabcode.compassabout.di.ServiceLocator
import com.gabcode.compassabout.domain.FindCharSequenceOccurrencesUseCase
import com.gabcode.compassabout.domain.FindEveryTenthCharacterUseCase
import com.gabcode.compassabout.domain.GrabContentUseCase
import kotlinx.coroutines.Dispatchers

class DomainModule : Module {

    override fun load(serviceLocator: ServiceLocator) {
        ServiceLocator.register(GrabContentUseCase(ServiceLocator.get()))
        ServiceLocator.register(FindCharSequenceOccurrencesUseCase(ServiceLocator.get(), Dispatchers.Default))
        ServiceLocator.register(FindEveryTenthCharacterUseCase(ServiceLocator.get(), Dispatchers.Default))
    }
}
