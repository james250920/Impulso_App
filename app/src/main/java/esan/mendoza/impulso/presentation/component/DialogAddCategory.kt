package esan.mendoza.impulso.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import esan.mendoza.impulso.presentation.component.IconPicker
import esan.mendoza.impulso.utils.DateUtils

@Composable
fun DialogAddCategory(
    show: Boolean,
    onDismiss: () -> Unit,
    onAccept: (String, String, String) -> Unit  // Agregamos parámetro para fecha
) {
    var nombre by remember { mutableStateOf("") }
    var selectedIcon by remember { mutableStateOf<IconPicker.IconOption?>(null) }
    var showIconPicker by remember { mutableStateOf(false) }

    // Obtener fecha actual usando la nueva utilidad
    val currentDate = remember {
        DateUtils.getCurrentDateTimeString()
    }

    if (show) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Agregar Categoría", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Nombre de la categoría") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Seleccionar ícono",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Botón para seleccionar ícono
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showIconPicker = !showIconPicker },
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (selectedIcon != null) {
                                Icon(
                                    imageVector = selectedIcon!!.icon,
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = selectedIcon!!.name,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            } else {
                                Icon(
                                    imageVector = IconPicker.availableIcons[0].icon,
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp),
                                    tint = MaterialTheme.colorScheme.outline
                                )
                                Text(
                                    text = "Seleccionar ícono",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.outline
                                )
                            }
                        }
                    }

                    // Grid de iconos (mostrar solo si showIconPicker es true)
                    if (showIconPicker) {
                        Spacer(modifier = Modifier.height(8.dp))

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(4),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(IconPicker.availableIcons) { iconOption ->
                                IconSelectionItem(
                                    iconOption = iconOption,
                                    isSelected = selectedIcon == iconOption,
                                    onIconSelected = {
                                        selectedIcon = iconOption
                                        showIconPicker = false
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
                        val iconName = selectedIcon?.name ?: IconPicker.availableIcons[0].name
                        onAccept(nombre, iconName, currentDate)  // Pasar fecha actual
                        // Reset state
                        nombre = ""
                        selectedIcon = null
                        showIconPicker = false
                    },
                    enabled = nombre.isNotBlank()
                ) {
                    Text("Crear")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    onDismiss()
                    // Reset state
                    nombre = ""
                    selectedIcon = null
                    showIconPicker = false
                }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun IconSelectionItem(
    iconOption: IconPicker.IconOption,
    isSelected: Boolean,
    onIconSelected: () -> Unit
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

    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onIconSelected() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = iconOption.icon,
            contentDescription = iconOption.name,
            modifier = Modifier.size(24.dp),
            tint = if (isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface
            }
        )
    }
}
