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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.BorderAll
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.DashboardCustomize
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import esan.mendoza.impulso.presentation.viewmodel.CategoryViewModel
import esan.mendoza.impulso.presentation.viewmodel.RecursoViewModel

@Composable
fun PrincipalScreen(
    categoryViewModel: CategoryViewModel,
    recursoViewModel: RecursoViewModel
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


