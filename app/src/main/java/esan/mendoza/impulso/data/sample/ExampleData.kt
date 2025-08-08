package esan.mendoza.impulso.data.sample

import esan.mendoza.impulso.data.local.entities.Category
import esan.mendoza.impulso.data.local.entities.Recurso

object ExampleData {

    // Categoría de ejemplo


    // Recurso de ejemplo
    val exampleRecurso = Recurso(
        id = -1, // ID negativo para distinguirlo de datos reales
        nombre = "¡Bienvenido a Impulso!",
        descripcion = "Este es un recurso de ejemplo para ayudarte a comenzar. Puedes crear tus propios recursos usando el botón + flotante. Una vez que agregues tu primer recurso, este ejemplo desaparecerá automáticamente.",
        categoriaId = -1,
        link = "https://ejemplo.com",
        createdAt = "2024-01-01",
        updatedAt = "2024-01-01",
        isFavorite = false
    )
}

