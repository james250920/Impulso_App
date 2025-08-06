package esan.mendoza.impulso.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import esan.mendoza.impulso.data.local.entities.Recurso
import esan.mendoza.impulso.data.local.entities.RecursoWithCategory

@Dao
interface RecursoDao {
    @Query("SELECT * FROM recurso")
    suspend fun getAllRecursos(): List<Recurso>

    @Query("SELECT * FROM recurso WHERE id = :id")
    suspend fun getRecursoById(id: Int): Recurso?

    @Query("SELECT * FROM recurso WHERE categoria_id = :categoryId")
    suspend fun getRecursosByCategory(categoryId: Int): List<Recurso>

    @Transaction
    @Query("SELECT * FROM recurso")
    suspend fun getRecursosWithCategory(): List<RecursoWithCategory>

    @Transaction
    @Query("SELECT * FROM recurso WHERE categoria_id = :categoryId")
    suspend fun getRecursosWithCategoryById(categoryId: Int): List<RecursoWithCategory>

    @Insert
    suspend fun insertRecurso(recurso: Recurso)

    @Update
    suspend fun updateRecurso(recurso: Recurso)

    @Delete
    suspend fun deleteRecurso(recurso: Recurso)
}