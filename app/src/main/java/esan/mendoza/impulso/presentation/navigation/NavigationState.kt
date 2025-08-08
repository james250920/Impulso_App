package esan.mendoza.impulso.presentation.navigation

enum class Screen {
    HOME,
    RESOURCE_DETAIL,
    FAVORITES,
    SETTINGS
}

data class NavigationState(
    val currentScreen: Screen = Screen.HOME,
    val selectedRecurso: esan.mendoza.impulso.data.local.entities.Recurso? = null,
    val selectedCategory: esan.mendoza.impulso.data.local.entities.Category? = null
)
