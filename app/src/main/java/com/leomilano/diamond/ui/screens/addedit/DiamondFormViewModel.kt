package com.leomilano.diamond.ui.screens.addedit

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.leomilano.diamond.DiamondApplication
import com.leomilano.diamond.data.entity.DiamondEntity
import com.leomilano.diamond.data.repository.DiamondRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

data class FormState(
    val nom: String = "",
    val carats: String = "",
    val prix: String = "",
    val couleur: String = "",
    val clarte: String = "",
    val notes: String = "",
    val imageUri: String? = null,
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val error: String? = null
)

class DiamondFormViewModel(
    application: Application,
    private val diamondId: Long? = null
) : AndroidViewModel(application) {

    private val repository: DiamondRepository = DiamondRepository(
        (application as DiamondApplication).database.diamondDao()
    )

    private val _formState = MutableStateFlow(FormState())
    val formState: StateFlow<FormState> = _formState.asStateFlow()

    init {
        if (diamondId != null) {
            loadDiamond(diamondId)
        }
    }

    private fun loadDiamond(id: Long) {
        viewModelScope.launch {
            _formState.value = _formState.value.copy(isLoading = true)
            val diamond = repository.getDiamondById(id)
            if (diamond != null) {
                _formState.value = FormState(
                    nom = diamond.nom,
                    carats = diamond.carats,
                    prix = diamond.prix,
                    couleur = diamond.couleur,
                    clarte = diamond.clarte,
                    notes = diamond.notes,
                    imageUri = diamond.imageUri
                )
            } else {
                _formState.value = _formState.value.copy(
                    isLoading = false,
                    error = "Diamant introuvable"
                )
            }
        }
    }

    fun onNomChange(value: String) {
        _formState.value = _formState.value.copy(nom = value)
    }

    fun onCaratsChange(value: String) {
        _formState.value = _formState.value.copy(carats = value)
    }

    fun onPrixChange(value: String) {
        _formState.value = _formState.value.copy(prix = value)
    }

    fun onCouleurChange(value: String) {
        _formState.value = _formState.value.copy(couleur = value)
    }

    fun onClarteChange(value: String) {
        _formState.value = _formState.value.copy(clarte = value)
    }

    fun onNotesChange(value: String) {
        _formState.value = _formState.value.copy(notes = value)
    }

    fun onImageSelected(uri: Uri) {
        val savedUri = saveImageToInternalStorage(uri, getApplication())
        _formState.value = _formState.value.copy(imageUri = savedUri?.toString())
    }

    fun saveDiamond(onSaved: () -> Unit) {
        val current = _formState.value
        if (current.nom.isBlank()) {
            _formState.value = current.copy(error = "Le nom est obligatoire")
            return
        }

        viewModelScope.launch {
            _formState.value = current.copy(isLoading = true)
            val diamond = DiamondEntity(
                id = diamondId ?: 0,
                nom = current.nom.trim(),
                carats = current.carats.trim(),
                prix = current.prix.trim(),
                couleur = current.couleur.trim(),
                clarte = current.clarte.trim(),
                notes = current.notes.trim(),
                imageUri = current.imageUri
            )
            if (diamondId != null) {
                repository.update(diamond)
            } else {
                repository.insert(diamond)
            }
            _formState.value = _formState.value.copy(isLoading = false, isSaved = true)
            onSaved()
        }
    }

    private fun saveImageToInternalStorage(sourceUri: Uri, context: Context): Uri? {
        return try {
            val inputStream = context.contentResolver.openInputStream(sourceUri) ?: return null
            val directory = File(context.filesDir, "diamond_images").apply { mkdirs() }
            val fileName = "diamond_${UUID.randomUUID()}.jpg"
            val outputFile = File(directory, fileName)
            FileOutputStream(outputFile).use { output ->
                inputStream.copyTo(output)
            }
            inputStream.close()
            Uri.fromFile(outputFile)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
