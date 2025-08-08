package esan.mendoza.impulso.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import esan.mendoza.impulso.data.local.entities.Recurso
import esan.mendoza.impulso.data.local.entities.RecursoWithCategory
import esan.mendoza.impulso.data.local.repositories.RecursoRepository
import esan.mendoza.impulso.data.sample.ExampleData
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
                val recursoList = recursoRepository.getAllRecursos()

                // Si no hay recursos reales, mostrar el recurso de ejemplo
                if (recursoList.isEmpty()) {
                    _recursos.value = listOf(ExampleData.exampleRecurso)
                } else {
                    _recursos.value = recursoList
                }

                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadRecursosWithCategory() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val recursoList = recursoRepository.getRecursosWithCategory()
                _recursosWithCategory.value = recursoList
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadRecursosByCategory(categoryId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val recursoList = recursoRepository.getRecursosByCategory(categoryId)
                _recursos.value = recursoList
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun insertRecurso(recurso: Recurso) {
        viewModelScope.launch {
            try {
                val result = recursoRepository.insertRecurso(recurso)
                result.fold(
                    onSuccess = {
                        loadRecursos()
                        loadRecursosWithCategory()
                        _error.value = null
                    },
                    onFailure = { exception ->
                        _error.value = exception.message
                    }
                )
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun updateRecurso(recurso: Recurso) {
        viewModelScope.launch {
            try {
                val result = recursoRepository.updateRecurso(recurso)
                result.fold(
                    onSuccess = {
                        loadRecursos()
                        loadRecursosWithCategory()
                        _error.value = null
                    },
                    onFailure = { exception ->
                        _error.value = exception.message
                    }
                )
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun deleteRecurso(recurso: Recurso) {
        viewModelScope.launch {
            try {
                recursoRepository.deleteRecurso(recurso)
                loadRecursos()
                loadRecursosWithCategory()
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun deleteRecursoById(id: Int) {
        viewModelScope.launch {
            try {
                recursoRepository.deleteRecursoById(id)
                loadRecursos()
                loadRecursosWithCategory()
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun searchRecursosByName(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val recursoList = recursoRepository.searchRecursosByName(query)
                _recursos.value = recursoList
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }

    fun addRecurso(nombre: String, descripcion: String, categoriaId: Int, link: String, createdAt: String) {
        viewModelScope.launch {
            try {
                val newRecurso = Recurso(
                    id = 0, // Room generará el ID automáticamente
                    nombre = nombre,
                    descripcion = descripcion,
                    categoriaId = categoriaId,
                    link = link,
                    createdAt = createdAt
                )
                val result = recursoRepository.insertRecurso(newRecurso)
                result.fold(
                    onSuccess = {
                        loadRecursos() // Recargar la lista (esto eliminará automáticamente el ejemplo)
                        loadRecursosWithCategory()
                        _error.value = null
                    },
                    onFailure = { exception ->
                        _error.value = exception.message
                    }
                )
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    // Métodos para manejar favoritos
    fun loadFavoriteRecursos() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val favoriteRecursos = recursoRepository.getFavoriteRecursos()

                // Para favoritos, no mostrar ejemplos - solo recursos reales marcados como favoritos
                _recursos.value = favoriteRecursos
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun toggleFavorite(recursoId: Int) {
        viewModelScope.launch {
            try {
                val recurso = recursoRepository.getRecursoById(recursoId)
                recurso?.let {
                    val updatedRecurso = it.copy(isFavorite = !it.isFavorite)
                    val result = recursoRepository.updateRecurso(updatedRecurso)
                    result.fold(
                        onSuccess = {
                            loadRecursos()
                            loadRecursosWithCategory()
                            _error.value = null
                        },
                        onFailure = { exception ->
                            _error.value = exception.message
                        }
                    )
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}
