package esan.mendoza.impulso.presentation.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

object IconPicker {
    data class IconOption(
        val name: String,
        val icon: ImageVector
    )

    val availableIcons = listOf(
        IconOption("Trabajo", Icons.Default.Work),
        IconOption("Educación", Icons.Default.School),
        IconOption("Entretenimiento", Icons.Default.Movie),
        IconOption("Tecnología", Icons.Default.Computer),
        IconOption("Deportes", Icons.Default.SportsBaseball),
        IconOption("Música", Icons.Default.MusicNote),
        IconOption("Cocina", Icons.Default.Restaurant),
        IconOption("Viajes", Icons.Default.Flight),
        IconOption("Salud", Icons.Default.LocalHospital),
        IconOption("Compras", Icons.Default.ShoppingCart),
        IconOption("Arte", Icons.Default.Palette),
        IconOption("Libros", Icons.AutoMirrored.Default.MenuBook),
        IconOption("Gaming", Icons.Default.SportsEsports),
        IconOption("Fotografía", Icons.Default.PhotoCamera),
        IconOption("Fitness", Icons.Default.FitnessCenter),
        IconOption("Finanzas", Icons.Default.AccountBalance),
        IconOption("Casa", Icons.Default.Home),
        IconOption("Herramientas", Icons.Default.Build),
        IconOption("Diseño", Icons.Default.Brush),
        IconOption("Ciencia", Icons.Default.Science),
        IconOption("Negocios", Icons.Default.Business),
        IconOption("Comunicación", Icons.AutoMirrored.Default.Chat),
        IconOption("Seguridad", Icons.Default.Security),
        IconOption("Naturaleza", Icons.Default.Park),
        IconOption("Automóvil", Icons.Default.DirectionsCar),
        IconOption("Mascotas", Icons.Default.Pets),
        IconOption("Juegos", Icons.Default.Casino),
        IconOption("IA", Icons.Default.Mood),
        IconOption("Favoritos", Icons.Default.Favorite),
        IconOption("Estrella", Icons.Default.Star),
        IconOption("Configuración", Icons.Default.Settings),
        IconOption("Moviles", Icons.Filled.MobileScreenShare)
    )

    fun getIconByName(name: String): ImageVector? {
        return availableIcons.find { it.name == name }?.icon
    }
}
