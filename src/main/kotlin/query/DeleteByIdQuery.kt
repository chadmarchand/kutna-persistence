package com.chadmarchand.kutna.persistence.query

import com.chadmarchand.kutna.persistence.mapper.EntityMapper
import org.springframework.jdbc.core.JdbcTemplate
import kotlin.reflect.KClass

class DeleteByIdQuery<T: Any> constructor(
    private val tableName: String,
    entityType: KClass<T>,
    private val jdbcTemplate: JdbcTemplate) {

    private val entityMapper = EntityMapper(entityType)

    fun execute(id: Long) {
        val deleteByClausesQuery = DeleteByClausesQuery(
            tableName,
            mapOf(Pair(entityMapper.idFieldName, id)),
            jdbcTemplate
        )
        deleteByClausesQuery.execute()
    }
}
