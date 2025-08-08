package esan.mendoza.impulso.presentation.homeScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddRoad
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import esan.mendoza.impulso.presentation.principalScreen.PrincipalScreen
import esan.mendoza.impulso.presentation.viewmodel.CategoryViewModel
import esan.mendoza.impulso.presentation.viewmodel.MainViewModel
import esan.mendoza.impulso.presentation.viewmodel.RecursoViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    mainViewModel: MainViewModel = hiltViewModel(),
    categoryViewModel: CategoryViewModel = hiltViewModel(),
    recursoViewModel: RecursoViewModel = hiltViewModel()
) {
    var fabsVisible by remember { mutableStateOf(false) }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    fabsVisible = !fabsVisible
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(
                    imageVector = if (fabsVisible) Icons.Default.Close else Icons.Default.Add,
                    contentDescription = if (fabsVisible) "Close" else "Open Menu",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))
            PrincipalScreen(
                categoryViewModel = categoryViewModel,
                recursoViewModel = recursoViewModel
            )
        }}

        // FABs secundarios posicionados en la esquina inferior derecha
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp), // Espacio para el FAB principal
            contentAlignment = Alignment.BottomEnd
        ) {
            FABColumn(
                isVisible = fabsVisible,
                fabsEnabled = true
            )
        }
    }
}



@Composable
fun CustomFAB(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = Modifier
            .padding(vertical = 4.dp, horizontal = 16.dp)

            .alpha(if (enabled) 1f else 0.9f),
        containerColor = if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(24.dp),
            tint = if (enabled) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun FABColumn(
    isVisible: Boolean,
    fabsEnabled: Boolean
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(
            initialOffsetY = { it },
            animationSpec = tween(300)
        ) + fadeIn(animationSpec = tween(300)),
        exit = slideOutVertically(
            targetOffsetY = { it },
            animationSpec = tween(300)
        ) + fadeOut(animationSpec = tween(300))
    ) {
        Column(
            modifier = Modifier
                .padding(end = 16.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.End
        ) {
            CustomFAB(
                icon = Icons.Default.AddRoad,
                contentDescription = "Añadir Recurso",
                onClick = {
                    if (fabsEnabled) {
                        // Acción de editar
                    }
                },
                enabled = fabsEnabled
            )

            CustomFAB(
                icon = Icons.Default.Category,
                contentDescription = "Añadir Categoria",
                onClick = {
                    if (fabsEnabled) {
                        // Acción de eliminar
                    }
                },
                enabled = fabsEnabled
            )

            CustomFAB(
                icon = Icons.Default.Favorite,
                contentDescription = "Mis Favoritos",
                onClick = {
                    if (fabsEnabled) {
                        // Acción de favorito
                    }
                },
                enabled = fabsEnabled
            )
            CustomFAB(
                icon = Icons.Default.Settings,
                contentDescription = "Configuración de categorias y recursos",
                onClick = {
                    if (fabsEnabled) {
                        // Acción de favorito
                    }
                },
                enabled = fabsEnabled
            )
        }
    }
}






