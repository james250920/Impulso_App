package esan.mendoza.impulso.presentation.principalScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BorderAll
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.DashboardCustomize
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import esan.mendoza.impulso.data.local.entities.Recurso
import esan.mendoza.impulso.presentation.component.IconPicker
import esan.mendoza.impulso.presentation.viewmodel.CategoryViewModel
import esan.mendoza.impulso.presentation.viewmodel.RecursoViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun PrincipalScreen(
    categoryViewModel: CategoryViewModel,
    recursoViewModel: RecursoViewModel,
    onResourceClick: (Recurso, esan.mendoza.impulso.data.local.entities.Category?) -> Unit = { _, _ -> }
) {
    val categories by categoryViewModel.categories.collectAsState()
    val recursos by recursoViewModel.recursos.collectAsState()
    val isLoading by categoryViewModel.isLoading.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Row {
            Icon(
                imageVector = Icons.Filled.DashboardCustomize,
                contentDescription = "Icono de flecha hacia abajo",
                modifier = Modifier
                    .padding(end = 8.dp)
                    .align(Alignment.CenterVertically).size(55.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Column {
                Text(
                    "IMPULSO",
                    style = MaterialTheme.typography.headlineLarge,
                )
                Text(
                    "Organiza tu universo",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(bottom = 5.dp)
                )
            }

        }
        Spacer(modifier = Modifier.height(10.dp))
        Buscador(
            categories = categories,
            onSearch = { query -> recursoViewModel.searchRecursosByName(query) },
            onCategorySelected = { categoryId -> 
                if (categoryId == -1) {
                    recursoViewModel.loadRecursos()
                } else {
                    recursoViewModel.loadRecursosByCategory(categoryId)
                }
            }
        )
        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
            text = "Recursos",
            style = MaterialTheme.typography.titleLarge,
        )
            Icon(
                imageVector = Icons.Default.BorderAll,
                contentDescription = "Icono de filtro",
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(30.dp),
                tint = MaterialTheme.colorScheme.primary
            )

        }
        RecursoGrid(
            recursos = recursos,
            categories = categories,
            onResourceClick = onResourceClick,
            onToggleFavorite = { recursoId ->
                recursoViewModel.toggleFavorite(recursoId)
            }
        )

    }
}

@Composable
fun Buscador(
    categories: List<esan.mendoza.impulso.data.local.entities.Category> = emptyList(),
    onSearch: (String) -> Unit = {},
    onCategorySelected: (Int) -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("Todas las categorias") }
    var selectedCategoryId by remember { mutableStateOf(-1) }
    var searchQuery by remember { mutableStateOf("") }

    val options = listOf("Todas las categorias") + categories.map { it.nombre }

    // Campo de búsqueda con ícono integrado
    OutlinedTextField(
        value = searchQuery,
        onValueChange = {
            searchQuery = it
            onSearch(it)
        },
        modifier = Modifier.fillMaxWidth(),
        label = { Text("Buscar") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Ícono de búsqueda",
                tint = MaterialTheme.colorScheme.primary
            )
        },
        singleLine = true,
        shape = RoundedCornerShape(25.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            cursorColor = MaterialTheme.colorScheme.primary
        )
    )
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(options.size) { index ->
            val option = options[index]
            val isSelected = selectedOption == option
            Button(
                onClick = {
                    selectedOption = option
                    if (index == 0) {
                        selectedCategoryId = -1
                        onCategorySelected(-1)
                    } else {
                        selectedCategoryId = categories[index - 1].id
                        onCategorySelected(categories[index - 1].id)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = option,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun RecursoGrid(
    recursos: List<Recurso>,
    categories: List<esan.mendoza.impulso.data.local.entities.Category>,
    onResourceClick: (Recurso, esan.mendoza.impulso.data.local.entities.Category?) -> Unit = { _, _ -> },
    onToggleFavorite: (Int) -> Unit = {}
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(3.dp),
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        items(recursos) { recurso ->
            RecursoCard(
                recurso = recurso,
                categories = categories,
                onResourceClick = onResourceClick,
                onToggleFavorite = onToggleFavorite
            )
        }
    }
}

fun formatFecha(fecha: String): String {
    // Suponiendo que recurso.createdAt es tipo "yyyy-MM-dd" o similar
    return try {
        val parser = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = parser.parse(fecha)
        val formatter = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        formatter.format(date!!)
    } catch (e: Exception) {
        fecha // Si falla, muestra la fecha original
    }
}

@Composable
fun RecursoCard(
    recurso: Recurso,
    categories: List<esan.mendoza.impulso.data.local.entities.Category>,
    onResourceClick: (Recurso, esan.mendoza.impulso.data.local.entities.Category?) -> Unit = { _, _ -> },
    onToggleFavorite: (Int) -> Unit = {}
) {
    val categoriaNombre = categories.find { it.id == recurso.categoriaId }?.nombre ?: "Sin categoría"
    val categoria = categories.find { it.id == recurso.categoriaId }
    val categoryIcon = categoria?.let {
        IconPicker.getIconByName(it.icono) ?: Icons.Default.Category
    } ?: Icons.Default.Category

    // Verificar si es el recurso de ejemplo
    val isExampleResource = recurso.id == -1

    Column {
        Card(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth()
                .height(120.dp),
            shape = RoundedCornerShape(16.dp),
            onClick = {
                // Solo permitir clic en recursos reales, no en el ejemplo
                if (!isExampleResource) {
                    onResourceClick(recurso, categoria)
                }
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = if (isExampleResource)
                                    MaterialTheme.colorScheme.tertiary.copy(alpha = 0.1f)
                                else
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = if (isExampleResource)
                                    MaterialTheme.colorScheme.tertiary
                                else
                                    MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(8.dp)
                            ),
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = categoryIcon,
                                contentDescription = null,
                                modifier = Modifier.size(12.dp),
                                tint = if (isExampleResource)
                                    MaterialTheme.colorScheme.tertiary
                                else
                                    MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = categoriaNombre,
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isExampleResource)
                                    MaterialTheme.colorScheme.tertiary
                                else
                                    MaterialTheme.colorScheme.primary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                    Row {
                        if (!isExampleResource) {
                            IconButton(
                                onClick = { onToggleFavorite(recurso.id) },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = if (recurso.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = "Favorito",
                                    modifier = Modifier.size(16.dp),
                                    tint = if (recurso.isFavorite) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                                )
                            }
                            IconButton(
                                onClick = { /* TODO: Implementar compartir */ },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "Compartir",
                                    modifier = Modifier.size(16.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        } else {
                            // Para el ejemplo, mostrar iconos deshabilitados
                            Icon(
                                imageVector = Icons.Default.FavoriteBorder,
                                contentDescription = "Ejemplo",
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.outline
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Ejemplo",
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.outline
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = recurso.nombre,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth(),
                    color = if (isExampleResource)
                        MaterialTheme.colorScheme.tertiary
                    else
                        MaterialTheme.colorScheme.onSurface
                )

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 2.dp),
                    color = if (isExampleResource)
                        MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f)
                    else
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                )
                Text(
                    text = recurso.descripcion,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth(),
                    color = if (isExampleResource)
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    else
                        MaterialTheme.colorScheme.onSurface
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Icono de recurso",
                    modifier = Modifier.size(10.dp),
                    tint = if (isExampleResource)
                        MaterialTheme.colorScheme.tertiary
                    else
                        MaterialTheme.colorScheme.primary
                )
                Text(
                    text = formatFecha(recurso.createdAt),
                    style = MaterialTheme.typography.labelSmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = if (isExampleResource)
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    else
                        MaterialTheme.colorScheme.onSurface
                )
            }
            Icon(
                imageVector = Icons.Default.Link,
                contentDescription = "Icono de recurso",
                modifier = Modifier.size(20.dp),
                tint = if (isExampleResource)
                    MaterialTheme.colorScheme.tertiary
                else
                    MaterialTheme.colorScheme.primary
            )
        }
    }
}
