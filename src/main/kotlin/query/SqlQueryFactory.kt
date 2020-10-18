package com.chadmarchand.kutna.persistence.query

import org.koin.core.KoinComponent
import org.koin.core.inject
import org.springframework.jdbc.core.JdbcTemplate
import kotlin.reflect.KClass

class SqlQueryFactory : KoinComponent {
    private val jdbcTemplate: JdbcTemplate by inject()

    fun <T : Any> createSelectListQuery(
        collectionName: String,
        entityType: KClass<T>,
        whereClauses: Map<String, Any>
    ): SelectListQuery<T> {

        return SelectListQuery(collectionName, entityType, whereClauses, jdbcTemplate)
    }

    fun <T : Any> createSelectObjectQuery(
        collectionName: String,
        entityType: KClass<T>,
        whereClauses: Map<String, Any>
    ): SelectObjectQuery<T> {

        return SelectObjectQuery(collectionName, entityType, whereClauses, jdbcTemplate)
    }

    fun <T : Any> createInsertAndReturnKeyQuery(
        collectionName: String,
        entityType: KClass<T>
    ): InsertAndReturnKeyQuery<T> {

        return InsertAndReturnKeyQuery(entityType, collectionName, jdbcTemplate)
    }

    fun <T : Any> createUpdateQuery(
        collectionName: String,
        entityType: KClass<T>,
        whereClauses: Map<String, Any>
    ): UpdateQuery<T> {

        return UpdateQuery(entityType, collectionName, whereClauses, jdbcTemplate)
    }

    fun <T : Any> createPatchQuery(
        collectionName: String,
        whereClauses: Map<String, String>
    ): PatchQuery {

        return PatchQuery(collectionName, whereClauses, jdbcTemplate)
    }

    fun <T : Any> createDeleteByIdQuery(
        collectionName: String,
        entityType: KClass<T>
    ): DeleteByIdQuery<T> {

        return DeleteByIdQuery(collectionName, entityType, jdbcTemplate)
    }

    fun createDeleteByClausesQuery(
        collectionName: String,
        whereClauses: Map<String, Any>
    ): DeleteByClausesQuery {

        return DeleteByClausesQuery(collectionName, whereClauses, jdbcTemplate)
    }
}
