package com.leomilano.diamond.ui.screens.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.leomilano.diamond.DiamondApplication
import com.leomilano.diamond.data.repository.DiamondRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: DiamondRepository = DiamondRepository(
        (application as DiamondApplication).database.diamondDao()
    )

    private val _uiState = MutableStateFlow<DashboardUiState>(DashboardUiState.Loading)
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        repository.allDiamonds
            .onEach { diamonds ->
                _uiState.value = if (diamonds.isEmpty()) {
                    DashboardUiState.Empty
                } else {
                    DashboardUiState.Success(diamonds)
                }
            }
            .launchIn(viewModelScope)
    }

    fun deleteDiamond(diamondId: Long) {
        viewModelScope.launch {
            repository.deleteById(diamondId)
        }
    }
}
