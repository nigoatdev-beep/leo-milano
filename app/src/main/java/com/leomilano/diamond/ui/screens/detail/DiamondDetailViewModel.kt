package com.leomilano.diamond.ui.screens.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.leomilano.diamond.DiamondApplication
import com.leomilano.diamond.data.entity.DiamondEntity
import com.leomilano.diamond.data.repository.DiamondRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class DetailUiState {
    data object Loading : DetailUiState()
    data class Success(val diamond: DiamondEntity) : DetailUiState()
    data object NotFound : DetailUiState()
}

class DiamondDetailViewModel(
    application: Application,
    private val diamondId: Long
) : AndroidViewModel(application) {

    private val repository: DiamondRepository = DiamondRepository(
        (application as DiamondApplication).database.diamondDao()
    )

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    init {
        loadDiamond()
    }

    private fun loadDiamond() {
        viewModelScope.launch {
            val diamond = repository.getDiamondById(diamondId)
            _uiState.value = if (diamond != null) {
                DetailUiState.Success(diamond)
            } else {
                DetailUiState.NotFound
            }
        }
    }

    fun deleteDiamond(onDeleted: () -> Unit) {
        viewModelScope.launch {
            repository.deleteById(diamondId)
            onDeleted()
        }
    }
}
