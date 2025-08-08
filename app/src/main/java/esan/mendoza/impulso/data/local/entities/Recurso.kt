package esan.mendoza.impulso.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "recurso",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["categoria_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["categoria_id"])]
)
data class Recurso(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "Nombre")
    val nombre: String,

    @ColumnInfo(name = "Descripcion")
    val descripcion: String,

    @ColumnInfo(name = "categoria_id")
    val categoriaId: Int,

    @ColumnInfo(name = "Link")
    val link: String,

    @ColumnInfo(name = "created_at")
    val createdAt: String, // Puedes usar Long para timestamp o Date

    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean = false
)