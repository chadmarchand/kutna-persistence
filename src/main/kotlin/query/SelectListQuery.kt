package com.chadmarchand.kutna.persistence.query

import com.chadmarchand.kutna.persistence.mapper.EntityMapper
import com.chadmarchand.kutna.persistence.mapper.SqlRowToEntityMapper

import org.springframework.jdbc.core.JdbcTemplate
import kotlin.reflect.KClass

class SelectListQuery<T: Any> constructor(
    private val collectionName: String,
    entityType: KClass<T>,
    private val whereClauses: Map<String, Any>,
    private val jdbcTemplate: JdbcTemplate) {

    private val entityMapper = EntityMapper(entityType)
    private val sqlRowToEntityMapper = SqlRowToEntityMapper(entityType, entityMapper)

    fun execute(): List<T> {
        val queryString = SqlQueryStringHelper.generateSelectString(
            collectionName,
            entityMapper.fieldNames,
            whereClauses.keys
        )

        return jdbcTemplate.query(queryString, sqlRowToEntityMapper, *(whereClauses.values.toTypedArray()))
    }
}
