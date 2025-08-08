package esan.mendoza.impulso.presentation.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import esan.mendoza.impulso.data.local.entities.Category
import esan.mendoza.impulso.data.local.entities.Recurso
import esan.mendoza.impulso.presentation.viewmodel.CategoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogEditRecurso(
    show: Boolean,
    recurso: Recurso?,
    onDismiss: () -> Unit,
    onAccept: (Recurso) -> Unit,
    categoryViewModel: CategoryViewModel = hiltViewModel()
) {
    var nombre by remember(recurso) { mutableStateOf(recurso?.nombre ?: "") }
    var descripcion by remember(recurso) { mutableStateOf(recurso?.descripcion ?: "") }
    var link by remember(recurso) { mutableStateOf(recurso?.link ?: "") }
    var selectedCategory by remember(recurso) {
        mutableStateOf<Category?>(null)
    }
    var isRecording by remember { mutableStateOf(false) }
    var showLinkField by remember(recurso) { mutableStateOf(recurso?.link?.isNotBlank() == true) }
    var voiceError by remember { mutableStateOf<String?>(null) }

    val categories by categoryViewModel.categories.collectAsState()
    val scrollState = rememberScrollState()

    // Inicializar categoría seleccionada
    LaunchedEffect(recurso, categories) {
        if (recurso != null && categories.isNotEmpty()) {
            selectedCategory = categories.find { it.id == recurso.categoriaId }
        }
    }

    // Configuración del reconocimiento de voz
    val voiceHelper = rememberVoiceRecognitionHelper(
        onResult = { result ->
            descripcion = if (descripcion.isEmpty()) result else "$descripcion $result"
            isRecording = false
            voiceError = null
        },
        onError = { error ->
            voiceError = error
            isRecording = false
        }
    )

    val requestMicrophonePermission = RequestMicrophonePermission(
        onPermissionGranted = {
            voiceHelper.startListening()
            isRecording = true
            voiceError = null
        },
        onPermissionDenied = {
            voiceError = "Permiso de micrófono denegado"
            isRecording = false
        }
    )

    // Limpiar recursos cuando se cierre el diálogo
    DisposableEffect(show) {
        onDispose {
            if (isRecording) {
                voiceHelper.stopListening()
                isRecording = false
            }
        }
    }

    if (show && recurso != null) {
        AlertDialog(
            onDismissRequest = {
                if (isRecording) {
                    voiceHelper.stopListening()
                    isRecording = false
                }
                onDismiss()
            },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 650.dp),
            title = {
                Text(
                    "Editar Recurso",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(scrollState),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Campo Nombre
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Nombre del recurso") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            focusedLabelColor = MaterialTheme.colorScheme.primary
                        )
                    )

                    // Campo Descripción con opción de voz
                    Text(
                        text = "Descripción",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {
                            OutlinedTextField(
                                value = descripcion,
                                onValueChange = { descripcion = it },
                                label = { Text("Describe tu recurso") },
                                modifier = Modifier.fillMaxWidth(),
                                minLines = 3,
                                maxLines = 4,
                                shape = RoundedCornerShape(8.dp),
                                trailingIcon = {
                                    IconButton(
                                        onClick = {
                                            if (isRecording) {
                                                voiceHelper.stopListening()
                                                isRecording = false
                                            } else {
                                                requestMicrophonePermission()
                                            }
                                        }
                                    ) {
                                        Icon(
                                            imageVector = if (isRecording) Icons.Default.MicOff else Icons.Default.Mic,
                                            contentDescription = if (isRecording) "Detener grabación" else "Grabar audio",
                                            tint = if (isRecording) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            )

                            if (isRecording) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(16.dp),
                                        strokeWidth = 2.dp,
                                        color = MaterialTheme.colorScheme.error
                                    )
                                    Text(
                                        text = "Escuchando...",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                            }

                            // Mostrar error de voz si existe
                            voiceError?.let { error ->
                                Spacer(modifier = Modifier.height(8.dp))
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.errorContainer
                                    )
                                ) {
                                    Text(
                                        text = error,
                                        modifier = Modifier.padding(8.dp),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onErrorContainer
                                    )
                                }
                            }
                        }
                    }

                    // Enlace opcional
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Enlace (opcional)",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )

                        Switch(
                            checked = showLinkField,
                            onCheckedChange = {
                                showLinkField = it
                                if (!it) link = ""
                            }
                        )
                    }

                    if (showLinkField) {
                        OutlinedTextField(
                            value = link,
                            onValueChange = { link = it },
                            label = { Text("URL o enlace") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Link,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            },
                            placeholder = { Text("https://ejemplo.com") },
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    // Selección de categoría
                    Text(
                        text = "Seleccionar categoría",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )

                    if (categories.isEmpty()) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer
                            )
                        ) {
                            Text(
                                text = "No hay categorías disponibles.",
                                modifier = Modifier.padding(16.dp),
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    } else {
                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(categories) { category ->
                                CategorySelectionCard(
                                    category = category,
                                    isSelected = selectedCategory == category,
                                    onCategorySelected = {
                                        selectedCategory = if (selectedCategory == category) null else category
                                    }
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val updatedRecurso = recurso.copy(
                            nombre = nombre,
                            descripcion = descripcion,
                            categoriaId = selectedCategory?.id ?: recurso.categoriaId,
                            link = if (showLinkField) link else ""
                        )
                        onAccept(updatedRecurso)

                        // Reset state
                        isRecording = false
                        voiceError = null
                        voiceHelper.destroy()
                    },
                    enabled = nombre.isNotBlank() && descripcion.isNotBlank() && selectedCategory != null,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Guardar Cambios")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        if (isRecording) {
                            voiceHelper.stopListening()
                        }
                        onDismiss()
                        // Reset state
                        isRecording = false
                        voiceError = null
                    },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}
