package com.gabcode.compassabout.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gabcode.compassabout.di.ServiceLocator

class AboutViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AboutViewModel::class.java)) {
            return AboutViewModel(ServiceLocator.get(), ServiceLocator.get(), ServiceLocator.get()) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
