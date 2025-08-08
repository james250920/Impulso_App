package esan.mendoza.impulso.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Category
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import esan.mendoza.impulso.data.local.entities.Category
import esan.mendoza.impulso.presentation.viewmodel.CategoryViewModel
import esan.mendoza.impulso.utils.DateUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogAddRecurso(
    show: Boolean,
    onDismiss: () -> Unit,
    onAccept: (String, String, String, String, String) -> Unit,
    categoryViewModel: CategoryViewModel = hiltViewModel()
) {
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var link by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var isRecording by remember { mutableStateOf(false) }
    var showLinkField by remember { mutableStateOf(false) }
    var voiceError by remember { mutableStateOf<String?>(null) }

    val categories by categoryViewModel.categories.collectAsState()
    val scrollState = rememberScrollState()

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

    // Usar la nueva utilidad de fechas para obtener la fecha actual
    val currentDate = remember {
        DateUtils.getCurrentDateTimeString()
    }

    if (show) {
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
                    "Nuevo Recurso",
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

                    // Botón para mostrar/ocultar campo de enlace
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "¿Deseas agregar un enlace?",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                        Switch(
                            checked = showLinkField,
                            onCheckedChange = { showLinkField = it }
                        )
                    }

                    // Campo de enlace (opcional)
                    if (showLinkField) {
                        OutlinedTextField(
                            value = link,
                            onValueChange = { link = it },
                            label = { Text("Enlace (opcional)") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Link,
                                    contentDescription = "Enlace"
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
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
                                text = "No hay categorías disponibles. Crea una categoría primero.",
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
                        val categoryId = selectedCategory?.id?.toString() ?: "0"
                        val finalLink = if (showLinkField) link else ""
                        onAccept(nombre, descripcion, categoryId, finalLink, currentDate)

                        // Reset state
                        nombre = ""
                        descripcion = ""
                        link = ""
                        selectedCategory = null
                        showLinkField = false
                        isRecording = false
                        voiceError = null

                        if (isRecording) {
                            voiceHelper.stopListening()
                        }
                    },
                    enabled = nombre.isNotBlank() && descripcion.isNotBlank() && selectedCategory != null,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Agregar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        if (isRecording) {
                            voiceHelper.stopListening()
                            isRecording = false
                        }
                        onDismiss()
                    },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun CategorySelectionCard(
    category: Category,
    isSelected: Boolean,
    onCategorySelected: () -> Unit
) {
    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surface
    }

    val borderColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
    }

    val categoryIcon = IconPicker.getIconByName(category.icono) ?: Icons.Default.Category

    Card(
        modifier = Modifier
            .width(120.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onCategorySelected() },
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 6.dp else 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = categoryIcon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = if (isSelected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
            )

            Text(
                text = category.nombre,
                style = MaterialTheme.typography.bodySmall,
                color = if (isSelected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                maxLines = 2,
                textAlign = TextAlign.Center
            )
        }
    }
}
