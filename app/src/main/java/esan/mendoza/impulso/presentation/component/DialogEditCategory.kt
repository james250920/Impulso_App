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
import esan.mendoza.impulso.data.local.entities.Category

@Composable
fun DialogEditCategory(
    show: Boolean,
    category: Category?,
    onDismiss: () -> Unit,
    onAccept: (Category) -> Unit
) {
    var nombre by remember(category) { mutableStateOf(category?.nombre ?: "") }
    var selectedIcon by remember(category) {
        mutableStateOf(
            category?.let { IconPicker.availableIcons.find { icon -> icon.name == it.icono } }
        )
    }
    var showIconPicker by remember { mutableStateOf(false) }

    if (show && category != null) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(
                    "Editar Categoría",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Campo nombre
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Nombre de la categoría") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            focusedLabelColor = MaterialTheme.colorScheme.primary
                        )
                    )

                    Text(
                        text = "Seleccionar ícono",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )

                    // Botón para seleccionar ícono
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showIconPicker = !showIconPicker },
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedIcon != null)
                                MaterialTheme.colorScheme.primaryContainer
                            else
                                MaterialTheme.colorScheme.surface
                        )
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
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.primary
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
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(4),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .padding(8.dp),
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
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val iconName = selectedIcon?.name ?: category.icono
                        val updatedCategory = category.copy(
                            nombre = nombre,
                            icono = iconName,

                        )
                        onAccept(updatedCategory)
                        // Reset state
                        showIconPicker = false
                    },
                    enabled = nombre.isNotBlank(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Guardar Cambios")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onDismiss()
                        // Reset state
                        nombre = category?.nombre ?: ""
                        selectedIcon = category?.let {
                            IconPicker.availableIcons.find { icon -> icon.name == it.icono }
                        }
                        showIconPicker = false
                    },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}
