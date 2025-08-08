package esan.mendoza.impulso.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import esan.mendoza.impulso.data.local.entities.Recurso
import esan.mendoza.impulso.data.local.entities.RecursoWithCategory
import esan.mendoza.impulso.data.local.repositories.RecursoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecursoViewModel @Inject constructor(
    private val recursoRepository: RecursoRepository
) : ViewModel() {

    private val _recursos = MutableStateFlow<List<Recurso>>(emptyList())
    val recursos: StateFlow<List<Recurso>> = _recursos.asStateFlow()

    private val _recursosWithCategory = MutableStateFlow<List<RecursoWithCategory>>(emptyList())
    val recursosWithCategory: StateFlow<List<RecursoWithCategory>> = _recursosWithCategory.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadRecursos()
        loadRecursosWithCategory()
    }

    fun loadRecursos() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val recursoList = recursoRepository.getAllRecursosWithExample()
                _recursos.value = recursoList
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
                _recursos.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadRecursosWithCategory() {
        viewModelScope.launch {
            try {
                val recursosWithCategory = recursoRepository.getRecursosWithCategory()
                _recursosWithCategory.value = recursosWithCategory
            } catch (e: Exception) {
                _error.value = e.message
                _recursosWithCategory.value = emptyList()
            }
        }
    }

    fun addRecurso(recurso: Recurso) {
        viewModelScope.launch {
            try {
                val result = recursoRepository.insertRecurso(recurso)
                if (result.isSuccess) {
                    loadRecursos() // Recargar la lista después de agregar
                    loadRecursosWithCategory() // Recargar recursos con categoría
                    _error.value = null
                } else {
                    _error.value = "Error al crear recurso: ${result.exceptionOrNull()?.message}"
                }
            } catch (e: Exception) {
                _error.value = "Error al crear recurso: ${e.message}"
            }
        }
    }

    fun updateRecurso(recurso: Recurso) {
        viewModelScope.launch {
            try {
                val result = recursoRepository.updateRecurso(recurso)
                if (result.isSuccess) {
                    loadRecursos() // Recargar la lista después de actualizar
                    loadRecursosWithCategory() // Recargar recursos con categoría
                    _error.value = null
                } else {
                    _error.value = "Error al actualizar recurso: ${result.exceptionOrNull()?.message}"
                }
            } catch (e: Exception) {
                _error.value = "Error al actualizar recurso: ${e.message}"
            }
        }
    }

    fun deleteRecurso(recurso: Recurso) {
        viewModelScope.launch {
            try {
                recursoRepository.deleteRecurso(recurso)
                loadRecursos() // Recargar la lista después de eliminar
                loadRecursosWithCategory() // Recargar recursos con categoría
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Error al eliminar recurso: ${e.message}"
            }
        }
    }

    fun getRecursosByCategory(categoryId: Int) {
        viewModelScope.launch {
            try {
                val recursos = recursoRepository.getRecursosByCategory(categoryId)
                _recursos.value = recursos
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Error al cargar recursos por categoría: ${e.message}"
            }
        }
    }

    fun loadRecursosByCategory(categoryId: Int) {
        getRecursosByCategory(categoryId)
    }

    fun searchRecursosByName(query: String) {
        viewModelScope.launch {
            try {
                val allRecursos = recursoRepository.getAllRecursos()
                val filteredRecursos = allRecursos.filter {
                    it.nombre.contains(query, ignoreCase = true) ||
                    it.descripcion.contains(query, ignoreCase = true)
                }
                _recursos.value = filteredRecursos
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Error al buscar recursos: ${e.message}"
            }
        }
    }

    fun toggleFavorite(recurso: Recurso) {
        viewModelScope.launch {
            try {
                val updatedRecurso = recurso.copy(isFavorite = !recurso.isFavorite)
                val result = recursoRepository.updateRecurso(updatedRecurso)
                if (result.isSuccess) {
                    loadRecursos()
                    loadRecursosWithCategory()
                    _error.value = null
                } else {
                    _error.value = "Error al actualizar favorito: ${result.exceptionOrNull()?.message}"
                }
            } catch (e: Exception) {
                _error.value = "Error al actualizar favorito: ${e.message}"
            }
        }
    }

    fun deleteRecursoById(id: Int) {
        viewModelScope.launch {
            try {
                val recurso = recursoRepository.getRecursoById(id)
                recurso?.let {
                    recursoRepository.deleteRecurso(it)
                    loadRecursos()
                    loadRecursosWithCategory()
                }
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Error al eliminar recurso: ${e.message}"
            }
        }
    }

    fun loadFavoriteRecursos() {
        viewModelScope.launch {
            try {
                val allRecursos = recursoRepository.getAllRecursos()
                val favoriteRecursos = allRecursos.filter { it.isFavorite }
                _recursos.value = favoriteRecursos
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Error al cargar favoritos: ${e.message}"
                _recursos.value = emptyList()
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}
