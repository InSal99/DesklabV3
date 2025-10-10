package com.edts.desklabv3.features.event.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MyEventsViewModelFactory(private val defaultFilter: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyEventsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MyEventsViewModel(defaultFilter) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}