package esan.mendoza.impulso.presentation.homeScreen

import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddRoad
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import esan.mendoza.impulso.data.local.entities.Recurso
import esan.mendoza.impulso.data.local.entities.Category
import esan.mendoza.impulso.presentation.principalScreen.PrincipalScreen
import esan.mendoza.impulso.presentation.resourceDetailScreen.ResourceDetailScreen
import esan.mendoza.impulso.presentation.navigation.Screen
import esan.mendoza.impulso.presentation.navigation.NavigationState
import esan.mendoza.impulso.presentation.viewmodel.CategoryViewModel
import esan.mendoza.impulso.presentation.viewmodel.RecursoViewModel
import esan.mendoza.impulso.presentation.favoritesScreen.FavoritesScreen
import esan.mendoza.impulso.presentation.settingsScreen.SettingsScreen

@Composable
fun HomeScreen(
    categoryViewModel: CategoryViewModel = hiltViewModel(),
    recursoViewModel: RecursoViewModel = hiltViewModel()
) {
    var navigationState by remember { mutableStateOf(NavigationState()) }
    var fabsVisible by remember { mutableStateOf(false) }
    var showAddCategoryDialog by remember { mutableStateOf(false) }
    var showAddRecursoDialog by remember { mutableStateOf(false) }

    // Configurar el manejo del botón de regreso del dispositivo
    val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    DisposableEffect(navigationState.currentScreen, backDispatcher) {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                when (navigationState.currentScreen) {
                    Screen.HOME -> {
                        // Si estamos en HOME, salir de la app (comportamiento por defecto)
                        isEnabled = false
                        backDispatcher?.onBackPressed()
                    }
                    Screen.RESOURCE_DETAIL -> {
                        // Regresar a HOME desde detalle de recurso
                        navigationState = navigationState.copy(
                            currentScreen = Screen.HOME,
                            selectedRecurso = null,
                            selectedCategory = null
                        )
                    }
                    Screen.FAVORITES -> {
                        // Regresar a HOME desde favoritos
                        navigationState = navigationState.copy(currentScreen = Screen.HOME)
                    }
                    Screen.SETTINGS -> {
                        // Regresar a HOME desde configuración
                        navigationState = navigationState.copy(currentScreen = Screen.HOME)
                    }
                }
            }
        }

        backDispatcher?.addCallback(callback)

        onDispose {
            callback.remove()
        }
    }

    Scaffold(
        floatingActionButton = {
            // Solo mostrar FAB principal en la pantalla HOME
            if (navigationState.currentScreen == Screen.HOME) {
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
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // Contenido principal basado en la pantalla actual
            when (navigationState.currentScreen) {
                Screen.HOME -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))
                        PrincipalScreen(
                            categoryViewModel = categoryViewModel,
                            recursoViewModel = recursoViewModel,
                            onResourceClick = { recurso, category ->
                                navigationState = navigationState.copy(
                                    currentScreen = Screen.RESOURCE_DETAIL,
                                    selectedRecurso = recurso,
                                    selectedCategory = category
                                )
                            }
                        )
                    }
                }

                Screen.RESOURCE_DETAIL -> {
                    navigationState.selectedRecurso?.let { recurso ->
                        ResourceDetailScreen(
                            recurso = recurso,
                            category = navigationState.selectedCategory,
                            onBackClick = {
                                navigationState = navigationState.copy(
                                    currentScreen = Screen.HOME,
                                    selectedRecurso = null,
                                    selectedCategory = null
                                )
                            },
                            onEditClick = {
                                // TODO: Implementar edición de recurso
                            }
                        )
                    }
                }

                Screen.FAVORITES -> {
                    FavoritesScreen(
                        onBackClick = {
                            navigationState = navigationState.copy(currentScreen = Screen.HOME)
                        },
                        onResourceClick = { recurso, category ->
                            navigationState = navigationState.copy(
                                currentScreen = Screen.RESOURCE_DETAIL,
                                selectedRecurso = recurso,
                                selectedCategory = category
                            )
                        },
                        categoryViewModel = categoryViewModel,
                        recursoViewModel = recursoViewModel
                    )
                }

                Screen.SETTINGS -> {
                    SettingsScreen(
                        onBackClick = {
                            navigationState = navigationState.copy(currentScreen = Screen.HOME)
                        },
                        categoryViewModel = categoryViewModel
                    )
                }
            }
        }

        // FABs secundarios posicionados en la esquina inferior derecha (solo en HOME)
        if (navigationState.currentScreen == Screen.HOME) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 80.dp), // Espacio para el FAB principal
                contentAlignment = Alignment.BottomEnd
            ) {
                FABColumn(
                    isVisible = fabsVisible,
                    fabsEnabled = true,
                    onAddCategory = { showAddCategoryDialog = true },
                    onAddRecurso = { showAddRecursoDialog = true },
                    onFavorites = {
                        navigationState = navigationState.copy(currentScreen = Screen.FAVORITES)
                        fabsVisible = false
                    },
                    onSettings = {
                        navigationState = navigationState.copy(currentScreen = Screen.SETTINGS)
                        fabsVisible = false
                    }
                )
            }
        }

        // Diálogos (disponibles en todas las pantallas)
        esan.mendoza.impulso.presentation.component.DialogAddCategory(
            show = showAddCategoryDialog,
            onDismiss = { showAddCategoryDialog = false },
            onAccept = { nombre, icono, createdAt ->
                val category = Category(
                    nombre = nombre, // Room generará el ID automáticamente
                    icono = icono,
                    createdAt = createdAt,
                    updatedAt = createdAt
                )
                categoryViewModel.addCategory(category)
                showAddCategoryDialog = false
            }
        )

        esan.mendoza.impulso.presentation.component.DialogAddRecurso(
            show = showAddRecursoDialog,
            onDismiss = { showAddRecursoDialog = false },
            onAccept = { nombre, descripcion, categoriaId, link, createdAt ->
                val recurso = Recurso(
                    id = 0, // Room generará el ID automáticamente
                    nombre = nombre,
                    descripcion = descripcion,
                    categoriaId = categoriaId.toIntOrNull() ?: 0,
                    link = link,
                    createdAt = createdAt,
                    updatedAt = createdAt,
                    isFavorite = false
                )
                recursoViewModel.addRecurso(recurso)
                showAddRecursoDialog = false
            },
            categoryViewModel = categoryViewModel
        )
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
            .padding(vertical = 2.dp, horizontal = 16.dp)
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
    fabsEnabled: Boolean,
    onAddCategory: () -> Unit,
    onAddRecurso: () -> Unit,
    onFavorites: () -> Unit,
    onSettings: () -> Unit
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
                .padding(end = 16.dp, bottom = 65.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.End
        ) {
            CustomFAB(
                icon = Icons.Default.AddRoad,
                contentDescription = "Añadir Recurso",
                onClick = {
                    if (fabsEnabled) {
                        onAddRecurso()
                    }
                },
                enabled = fabsEnabled
            )

            CustomFAB(
                icon = Icons.Default.Category,
                contentDescription = "Añadir Categoria",
                onClick = {
                    if (fabsEnabled) {
                        onAddCategory()
                    }
                },
                enabled = fabsEnabled
            )

            CustomFAB(
                icon = Icons.Default.Favorite,
                contentDescription = "Mis Favoritos",
                onClick = {
                    if (fabsEnabled) {
                        onFavorites()
                    }
                },
                enabled = fabsEnabled
            )

            CustomFAB(
                icon = Icons.Default.Settings,
                contentDescription = "Configuración de categorias y recursos",
                onClick = {
                    if (fabsEnabled) {
                        onSettings()
                    }
                },
                enabled = fabsEnabled
            )
        }
    }
}
