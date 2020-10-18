package com.chadmarchand.kutna.persistence.query

import com.chadmarchand.kutna.persistence.mapper.EntityMapper
import com.chadmarchand.kutna.persistence.mapper.SqlRowToEntityMapper
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import java.lang.RuntimeException
import kotlin.reflect.KClass

class SelectObjectQuery<T: Any> constructor(
    private val collectionName: String,
    private val entityType: KClass<T>,
    private val whereClauses: Map<String, Any>,
    private val jdbcTemplate: JdbcTemplate) {

    private val entityMapper = EntityMapper(entityType)

    fun execute(): T {
        val queryString = SqlQueryStringHelper.generateSelectString(collectionName, entityMapper.fieldNames, whereClauses.keys)

        try {
            val result: T? = jdbcTemplate.queryForObject(
                queryString,
                SqlRowToEntityMapper(entityType, entityMapper),
                *whereClauses.values.toTypedArray()
            )
            result ?: throw RuntimeException("Object not found")
            return result
        } catch (e: EmptyResultDataAccessException) {
            throw RuntimeException("Object not found")
        }
    }
}
