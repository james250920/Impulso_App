package esan.mendoza.impulso.data.local.repositories

import esan.mendoza.impulso.data.local.dao.CategoryDao
import esan.mendoza.impulso.data.local.dao.RecursoDao
import esan.mendoza.impulso.data.local.entities.Recurso
import esan.mendoza.impulso.data.local.entities.RecursoWithCategory

class RecursoRepository(
    private val recursoDao: RecursoDao,
    private val categoryDao: CategoryDao
) {

    // Obtener todos los recursos
    suspend fun getAllRecursos(): List<Recurso> {
        return recursoDao.getAllRecursos()
    }

    // Obtener recurso por ID
    suspend fun getRecursoById(id: Int): Recurso? {
        return recursoDao.getRecursoById(id)
    }

    // Obtener recursos por categoría
    suspend fun getRecursosByCategory(categoryId: Int): List<Recurso> {
        return recursoDao.getRecursosByCategory(categoryId)
    }

    // Obtener recursos con información de categoría
    suspend fun getRecursosWithCategory(): List<RecursoWithCategory> {
        return recursoDao.getRecursosWithCategory()
    }

    // Obtener recursos con categoría por ID de categoría
    suspend fun getRecursosWithCategoryById(categoryId: Int): List<RecursoWithCategory> {
        return recursoDao.getRecursosWithCategoryById(categoryId)
    }

    // Insertar nuevo recurso (con validación de categoría)
    suspend fun insertRecurso(recurso: Recurso): Result<Unit> {
        return try {
            // Verificar que la categoría existe
            val categoryExists = categoryDao.getCategoryById(recurso.categoriaId) != null
            if (!categoryExists) {
                Result.failure(IllegalArgumentException("La categoría con ID ${recurso.categoriaId} no existe"))
            } else {
                // Insertar el nuevo recurso
                recursoDao.insertRecurso(recurso)

                // Eliminar el recurso de ejemplo si existe (ID = -1) y el nuevo recurso es real (ID > 0)
                if (recurso.id > 0) {
                    val exampleRecurso = getRecursoById(-1)
                    exampleRecurso?.let {
                        deleteRecurso(it)
                    }
                }

                Result.success(Unit)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Actualizar recurso
    suspend fun updateRecurso(recurso: Recurso): Result<Unit> {
        return try {
            // Verificar que la categoría existe
            val categoryExists = categoryDao.getCategoryById(recurso.categoriaId) != null
            if (!categoryExists) {
                Result.failure(IllegalArgumentException("La categoría con ID ${recurso.categoriaId} no existe"))
            } else {
                recursoDao.updateRecurso(recurso)
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Eliminar recurso
    suspend fun deleteRecurso(recurso: Recurso) {
        recursoDao.deleteRecurso(recurso)
    }

    // Eliminar recurso por ID
    suspend fun deleteRecursoById(id: Int) {
        val recurso = getRecursoById(id)
        recurso?.let { deleteRecurso(it) }
    }

    // Eliminar todos los recursos de una categoría
    suspend fun deleteRecursosByCategory(categoryId: Int) {
        val recursos = getRecursosByCategory(categoryId)
        recursos.forEach { deleteRecurso(it) }
    }

    // Verificar si existe un recurso
    suspend fun recursoExists(id: Int): Boolean {
        return getRecursoById(id) != null
    }

    // Contar recursos por categoría
    suspend fun countRecursosByCategory(categoryId: Int): Int {
        return getRecursosByCategory(categoryId).size
    }

    // Buscar recursos por nombre
    suspend fun searchRecursosByName(query: String): List<Recurso> {
        return getAllRecursos().filter {
            it.nombre.contains(query, ignoreCase = true)
        }
    }

    // Buscar recursos por descripción
    suspend fun searchRecursosByDescription(query: String): List<Recurso> {
        return getAllRecursos().filter {
            it.descripcion.contains(query, ignoreCase = true)
        }
    }

    // Buscar recursos por nombre o descripción
    suspend fun searchRecursos(query: String): List<RecursoWithCategory> {
        return getRecursosWithCategory().filter {
            it.recurso.nombre.contains(query, ignoreCase = true) ||
                    it.recurso.descripcion.contains(query, ignoreCase = true)
        }
    }

    // Obtener recursos favoritos
    suspend fun getFavoriteRecursos(): List<Recurso> {
        return getAllRecursos().filter { it.isFavorite }
    }

    // Obtener recursos favoritos con categoría
    suspend fun getFavoriteRecursosWithCategory(): List<RecursoWithCategory> {
        return getRecursosWithCategory().filter { it.recurso.isFavorite }
    }

    // Alternar estado de favorito
    suspend fun toggleFavorite(recursoId: Int): Result<Unit> {
        return try {
            val recurso = getRecursoById(recursoId)
            if (recurso != null) {
                val updatedRecurso = recurso.copy(isFavorite = !recurso.isFavorite)
                updateRecurso(updatedRecurso)
            } else {
                Result.failure(IllegalArgumentException("Recurso no encontrado"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Verificar si hay recursos reales (excluyendo el ejemplo con ID -1)
    suspend fun hasRealRecursos(): Boolean {
        return getAllRecursos().any { it.id > 0 }
    }

    // Obtener todos los recursos incluyendo el ejemplo si no hay recursos reales
    suspend fun getAllRecursosWithExample(): List<Recurso> {
        val realRecursos = getAllRecursos().filter { it.id > 0 }
        return if (realRecursos.isEmpty()) {
            // Si no hay recursos reales, incluir el recurso de ejemplo
            val exampleExists = getAllRecursos().any { it.id == -1 }
            if (exampleExists) {
                getAllRecursos()
            } else {
                // Si no existe el ejemplo en la BD, agregarlo temporalmente a la lista
                getAllRecursos() + esan.mendoza.impulso.data.sample.ExampleData.exampleRecurso
            }
        } else {
            realRecursos
        }
    }
}
