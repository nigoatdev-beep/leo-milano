package com.leomilano.diamond.ui.screens.addedit

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DiamondFormViewModelFactory(
    private val application: Application,
    private val diamondId: Long? = null
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DiamondFormViewModel::class.java)) {
            return DiamondFormViewModel(application, diamondId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
