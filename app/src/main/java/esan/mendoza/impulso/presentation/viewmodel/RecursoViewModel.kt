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
                val recursoList = recursoRepository.getAllRecursos()
                _recursos.value = recursoList
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
}
