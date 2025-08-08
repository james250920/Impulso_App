package esan.mendoza.impulso.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import esan.mendoza.impulso.data.local.entities.Category
import esan.mendoza.impulso.data.local.repositories.CategoryRepository
import esan.mendoza.impulso.data.sample.ExampleData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadCategories()
    }

    fun loadCategories() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val categoryList = categoryRepository.getAllCategories()
                _categories.value = categoryList
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
                _categories.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addCategory(category: Category) {
        viewModelScope.launch {
            try {
                categoryRepository.insertCategory(category)
                loadCategories() // Recargar la lista después de agregar
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Error al crear categoría: ${e.message}"
            }
        }
    }

    fun updateCategory(category: Category) {
        viewModelScope.launch {
            try {
                categoryRepository.updateCategory(category)
                loadCategories() // Recargar la lista después de actualizar
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Error al actualizar categoría: ${e.message}"
            }
        }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch {
            try {
                categoryRepository.deleteCategory(category)
                loadCategories() // Recargar la lista después de eliminar
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Error al eliminar categoría: ${e.message}"
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}
