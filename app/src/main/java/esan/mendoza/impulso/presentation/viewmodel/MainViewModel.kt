package esan.mendoza.impulso.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import esan.mendoza.impulso.data.local.repositories.CategoryRepository
import esan.mendoza.impulso.data.local.repositories.RecursoRepository
import esan.mendoza.impulso.data.sample.SampleData
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val recursoRepository: RecursoRepository
) : ViewModel() {

    init {
        initializeSampleData()
    }

    private fun initializeSampleData() {
        viewModelScope.launch {
            try {
                // Verificar si ya hay datos
                val existingCategories = categoryRepository.getAllCategories()
                if (existingCategories.isEmpty()) {
                    // Insertar categorías de muestra
                    SampleData.sampleCategories.forEach { category ->
                        categoryRepository.insertCategory(category)
                    }
                    
                    // Insertar recursos de muestra
                    SampleData.sampleRecursos.forEach { recurso ->
                        recursoRepository.insertRecurso(recurso)
                    }
                }
            } catch (e: Exception) {
                // Log error or handle appropriately
                e.printStackTrace()
            }
        }
    }
}
