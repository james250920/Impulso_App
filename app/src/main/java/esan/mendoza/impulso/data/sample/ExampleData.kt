package esan.mendoza.impulso.data.sample

import esan.mendoza.impulso.data.local.entities.Category
import esan.mendoza.impulso.data.local.entities.Recurso

object ExampleData {

    // Categoría de ejemplo
    val exampleCategory = Category(
        id = -1, // ID negativo para distinguirlo de datos reales
        nombre = "Ejemplo",
        icono = "ic_baseline_star_24",
        createdAt = "2024-01-01",
        updatedAt = "2024-01-01"

    )

    // Recurso de ejemplo
    val exampleRecurso = Recurso(
        id = -1, // ID negativo para distinguirlo de datos reales
        nombre = "¡Bienvenido a Impulso!",
        descripcion = "Este es un recurso de ejemplo para ayudarte a comenzar.  Preciona +",
        categoriaId = -1,
        link = "https://ejemplo.com",
        createdAt = "by menfroyt Dev",
        updatedAt = "2024-01-01",
        isFavorite = false
    )
}
