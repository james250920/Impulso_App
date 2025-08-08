package esan.mendoza.impulso.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category")
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0, // Valor por defecto para permitir autoGenerate

    @ColumnInfo(name = "Nombre")
    val nombre: String,

    @ColumnInfo(name = "icono")
    val icono: String,

    @ColumnInfo(name = "created_at")
    val createdAt: String,

    @ColumnInfo(name = "updated_at")
    val updatedAt: String
)