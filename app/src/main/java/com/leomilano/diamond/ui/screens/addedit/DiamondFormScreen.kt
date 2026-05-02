package com.leomilano.diamond.ui.screens.addedit

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.leomilano.diamond.R
import com.leomilano.diamond.ui.components.LuxuryButton
import com.leomilano.diamond.ui.components.LuxuryTextField
import java.io.File
import java.util.UUID

@Composable
fun DiamondFormScreen(
    diamondId: Long? = null,
    onNavigateBack: () -> Unit,
    viewModel: DiamondFormViewModel = viewModel(
        factory = DiamondFormViewModelFactory(
            LocalContext.current.applicationContext as android.app.Application,
            diamondId
        )
    )
) {
    val formState by viewModel.formState.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()

    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri: Uri? ->
            uri?.let { viewModel.onImageSelected(it) }
        }
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                tempCameraUri?.let { viewModel.onImageSelected(it) }
            }
        }
    )

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                val authority = context.packageName + ".fileprovider"
                val tempFile = File(context.cacheDir, "camera_${UUID.randomUUID()}.jpg").apply { createNewFile() }
                val uri = FileProvider.getUriForFile(context, authority, tempFile)
                tempCameraUri = uri
                cameraLauncher.launch(uri)
            }
        }
    )

    LaunchedEffect(formState.error) {
        formState.error?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 12.dp)
                    .background(MaterialTheme.colorScheme.background),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Retour",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                Text(
                    text = if (diamondId != null) "Modifier" else "Nouveau Diamant",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Light,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (formState.isLoading && diamondId != null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.secondary)
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                ) {
                    // Image section
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn(tween(400)) + slideInVertically(
                            initialOffsetY = { it / 6 },
                            animationSpec = tween(400)
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1.2f)
                                .clip(RoundedCornerShape(24.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            if (formState.imageUri != null) {
                                AsyncImage(
                                    model = formState.imageUri,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                Text(
                                    text = "Aucune image",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Image actions
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        IconButton(
                            onClick = {
                                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                                    val authority = context.packageName + ".fileprovider"
                                    val tempFile = File(context.cacheDir, "camera_${UUID.randomUUID()}.jpg").apply { createNewFile() }
                                    val uri = FileProvider.getUriForFile(context, authority, tempFile)
                                    tempCameraUri = uri
                                    cameraLauncher.launch(uri)
                                } else {
                                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                                }
                            },
                            modifier = Modifier
                                .size(56.dp)
                                .background(
                                    MaterialTheme.colorScheme.surface,
                                    CircleShape
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Filled.CameraAlt,
                                contentDescription = "Appareil photo",
                                tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        IconButton(
                            onClick = {
                                imagePickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                            modifier = Modifier
                                .size(56.dp)
                                .background(
                                    MaterialTheme.colorScheme.surface,
                                    CircleShape
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Filled.PhotoLibrary,
                                contentDescription = "Galerie",
                                tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Form fields
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn(tween(500, delayMillis = 100)) + slideInVertically(
                            initialOffsetY = { it / 8 },
                            animationSpec = tween(500, delayMillis = 100)
                        )
                    ) {
                        Column {
                            LuxuryTextField(
                                value = formState.nom,
                                onValueChange = viewModel::onNomChange,
                                label = "Nom"
                            )
                            LuxuryTextField(
                                value = formState.carats,
                                onValueChange = viewModel::onCaratsChange,
                                label = "Carats",
                                keyboardType = KeyboardType.Decimal
                            )
                            LuxuryTextField(
                                value = formState.prix,
                                onValueChange = viewModel::onPrixChange,
                                label = "Prix (€)",
                                keyboardType = KeyboardType.Number
                            )
                            LuxuryTextField(
                                value = formState.stock,
                                onValueChange = viewModel::onStockChange,
                                label = "Stock",
                                keyboardType = KeyboardType.Number
                            )
                            LuxuryTextField(
                                value = formState.couleur,
                                onValueChange = viewModel::onCouleurChange,
                                label = "Couleur"
                            )
                            LuxuryTextField(
                                value = formState.clarte,
                                onValueChange = viewModel::onClarteChange,
                                label = "Clarté"
                            )
                            LuxuryTextField(
                                value = formState.notes,
                                onValueChange = viewModel::onNotesChange,
                                label = "Notes supplémentaires",
                                singleLine = false,
                                imeAction = ImeAction.Done,
                                maxLines = 5
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    LuxuryButton(
                        text = if (diamondId != null) "Enregistrer" else "Ajouter",
                        onClick = { viewModel.saveDiamond(onNavigateBack) }
                    )

                    Spacer(modifier = Modifier.height(48.dp))
                }
            }
        }
    }
}
