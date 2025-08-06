package esan.mendoza.impulso.data.local.repositories

import esan.mendoza.impulso.data.local.entities.Category

data class CategoryWithCount(
    val category: Category,
    val resourceCount: Int
)