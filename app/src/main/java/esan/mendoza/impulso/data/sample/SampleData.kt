package esan.mendoza.impulso.data.sample

import esan.mendoza.impulso.data.local.entities.Category
import esan.mendoza.impulso.data.local.entities.Recurso

object SampleData {
    
    val sampleCategories = listOf(
        Category(
            id = 1,
            nombre = "Desarrollo Web",
            icono = "web"
        ),
        Category(
            id = 2,
            nombre = "Inteligencia Artificial",
            icono = "ai"
        ),
        Category(
            id = 3,
            nombre = "Diseño UI/UX",
            icono = "design"
        ),
        Category(
            id = 4,
            nombre = "Bases de Datos",
            icono = "database"
        )
    )
    
    val sampleRecursos = listOf(
        Recurso(
            id = 1,
            nombre = "React Documentation",
            descripcion = "Documentación oficial de React para desarrolladores web",
            categoriaId = 1,
            link = "https://reactjs.org/docs",
            createdAt = "2024-01-15T10:30:00Z"
        ),
        Recurso(
            id = 2,
            nombre = "Vue.js Guide",
            descripcion = "Guía completa de Vue.js para principiantes y expertos",
            categoriaId = 1,
            link = "https://vuejs.org/guide",
            createdAt = "2024-01-16T14:20:00Z"
        ),
        Recurso(
            id = 3,
            nombre = "TensorFlow Tutorials",
            descripcion = "Tutoriales oficiales de TensorFlow para machine learning",
            categoriaId = 2,
            link = "https://tensorflow.org/tutorials",
            createdAt = "2024-01-17T09:15:00Z"
        ),
        Recurso(
            id = 4,
            nombre = "PyTorch Learning",
            descripcion = "Recursos de aprendizaje para PyTorch y deep learning",
            categoriaId = 2,
            link = "https://pytorch.org/tutorials",
            createdAt = "2024-01-18T16:45:00Z"
        ),
        Recurso(
            id = 5,
            nombre = "Figma Design System",
            descripcion = "Sistema de diseño completo en Figma para UI/UX",
            categoriaId = 3,
            link = "https://figma.com/design-systems",
            createdAt = "2024-01-19T11:30:00Z"
        ),
        Recurso(
            id = 6,
            nombre = "PostgreSQL Documentation",
            descripcion = "Documentación completa de PostgreSQL",
            categoriaId = 4,
            link = "https://postgresql.org/docs",
            createdAt = "2024-01-20T13:20:00Z"
        )
    )
}
