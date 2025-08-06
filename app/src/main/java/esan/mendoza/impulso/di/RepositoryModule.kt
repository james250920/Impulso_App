package esan.mendoza.impulso.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import esan.mendoza.impulso.data.local.dao.CategoryDao
import esan.mendoza.impulso.data.local.dao.RecursoDao
import esan.mendoza.impulso.data.local.repositories.CategoryRepository
import esan.mendoza.impulso.data.local.repositories.RecursoRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideCategoryRepository(categoryDao: CategoryDao): CategoryRepository {
        return CategoryRepository(categoryDao)
    }

    @Provides
    @Singleton
    fun provideRecursoRepository(
        recursoDao: RecursoDao,
        categoryDao: CategoryDao
    ): RecursoRepository {
        return RecursoRepository(recursoDao, categoryDao)
    }
}
