package com.leomilano.diamond.ui.screens.dashboard

import com.leomilano.diamond.data.entity.DiamondEntity

sealed class DashboardUiState {
    data object Loading : DashboardUiState()
    data object Empty : DashboardUiState()
    data class Success(val diamonds: List<DiamondEntity>) : DashboardUiState()
}
