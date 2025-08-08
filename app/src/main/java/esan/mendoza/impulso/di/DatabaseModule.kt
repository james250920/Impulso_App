package esan.mendoza.impulso.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import esan.mendoza.impulso.data.db.AppDatabase
import esan.mendoza.impulso.data.local.dao.CategoryDao
import esan.mendoza.impulso.data.local.dao.RecursoDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "app_database"
        )
        .fallbackToDestructiveMigration() // Permite recrear la DB si hay problemas de migración
        .build()
    }

    @Provides
    fun provideCategoryDao(database: AppDatabase): CategoryDao {
        return database.categoryDao()
    }

    @Provides
    fun provideRecursoDao(database: AppDatabase): RecursoDao {
        return database.recursoDao()
    }
}
