package com.leomilano.diamond.ui.screens.detail

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DiamondDetailViewModelFactory(
    private val application: Application,
    private val diamondId: Long
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DiamondDetailViewModel::class.java)) {
            return DiamondDetailViewModel(application, diamondId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
