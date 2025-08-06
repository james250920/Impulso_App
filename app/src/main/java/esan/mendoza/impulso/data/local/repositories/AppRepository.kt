package esan.mendoza.impulso.data.local.repositories

class AppRepository(
    private val categoryRepository: CategoryRepository,
    private val recursoRepository: RecursoRepository
) {

    // Obtener estadísticas generales
    suspend fun getDashboardStats(): DashboardStats {
        val totalCategories = categoryRepository.getAllCategories().size
        val totalRecursos = recursoRepository.getAllRecursos().size

        return DashboardStats(
            totalCategories = totalCategories,
            totalRecursos = totalRecursos
        )
    }

    // Obtener categorías con conteo de recursos
    suspend fun getCategoriesWithResourceCount(): List<CategoryWithCount> {
        val categories = categoryRepository.getAllCategories()
        return categories.map { category ->
            val resourceCount = recursoRepository.countRecursosByCategory(category.id)
            CategoryWithCount(category, resourceCount)
        }
    }

    // Eliminar categoría y todos sus recursos
    suspend fun deleteCategoryWithResources(categoryId: Int) {
        recursoRepository.deleteRecursosByCategory(categoryId)
        categoryRepository.deleteCategoryById(categoryId)
    }

    // Migrar recursos de una categoría a otra
    suspend fun migrateRecursos(fromCategoryId: Int, toCategoryId: Int): Result<Unit> {
        return try {
            // Verificar que la categoría destino existe
            val targetCategoryExists = categoryRepository.categoryExists(toCategoryId)
            if (!targetCategoryExists) {
                return Result.failure(IllegalArgumentException("La categoría destino no existe"))
            }

            val recursos = recursoRepository.getRecursosByCategory(fromCategoryId)
            recursos.forEach { recurso ->
                val updatedRecurso = recurso.copy(categoriaId = toCategoryId)
                recursoRepository.updateRecurso(updatedRecurso)
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}