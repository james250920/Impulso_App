package esan.mendoza.impulso.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category")
data class Category(
    @PrimaryKey
    val id: Int,

    @ColumnInfo(name = "Nombre")
    val nombre: String,

    @ColumnInfo(name = "icono")
    val icono: String
)