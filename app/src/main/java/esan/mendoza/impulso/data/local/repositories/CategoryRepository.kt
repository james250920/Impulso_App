package esan.mendoza.impulso.data.local.repositories

import esan.mendoza.impulso.data.local.dao.CategoryDao
import esan.mendoza.impulso.data.local.entities.Category

class CategoryRepository(private val categoryDao: CategoryDao) {

    // Obtener todas las categorías
    suspend fun getAllCategories(): List<Category> {
        return categoryDao.getAllCategories()
    }

    // Obtener categoría por ID
    suspend fun getCategoryById(id: Int): Category? {
        return categoryDao.getCategoryById(id)
    }

    // Insertar nueva categoría
    suspend fun insertCategory(category: Category) {
        categoryDao.insertCategory(category)
    }

    // Actualizar categoría
    suspend fun updateCategory(category: Category) {
        categoryDao.updateCategory(category)
    }

    // Eliminar categoría
    suspend fun deleteCategory(category: Category) {
        categoryDao.deleteCategory(category)
    }

    // Eliminar categoría por ID
    suspend fun deleteCategoryById(id: Int) {
        val category = getCategoryById(id)
        category?.let { deleteCategory(it) }
    }

    // Verificar si existe una categoría
    suspend fun categoryExists(id: Int): Boolean {
        return getCategoryById(id) != null
    }
}