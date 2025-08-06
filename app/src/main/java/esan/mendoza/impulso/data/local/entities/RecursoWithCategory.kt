package esan.mendoza.impulso.data.local.entities

import androidx.room.Embedded
import androidx.room.Relation

data class RecursoWithCategory(
    @Embedded val recurso: Recurso,
    @Relation(
        parentColumn = "user_id",
        entityColumn = "id"
    )
    val category: Category
)