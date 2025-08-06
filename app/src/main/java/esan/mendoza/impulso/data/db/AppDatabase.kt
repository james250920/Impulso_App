package esan.mendoza.impulso.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import esan.mendoza.impulso.data.local.dao.CategoryDao
import esan.mendoza.impulso.data.local.dao.RecursoDao
import esan.mendoza.impulso.data.local.entities.Category
import esan.mendoza.impulso.data.local.entities.Recurso

@Database(
    entities = [Category::class, Recurso::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun recursoDao(): RecursoDao
}
