package com.chadmarchand.kutna.persistence.query

import com.chadmarchand.kutna.common.types.Id
import com.chadmarchand.kutna.persistence.mapper.EntityMapper
import mu.KotlinLogging
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import kotlin.reflect.KClass

private val logger = KotlinLogging.logger {}

class InsertAndReturnKeyQuery<T: Any>(
    entityType: KClass<T>,
    private val collectionName: String,
    private val jdbcTemplate: JdbcTemplate) {

    private val entityMapper = EntityMapper(entityType)

    fun execute(entity: T): Id {
        val insert = SimpleJdbcInsert(jdbcTemplate)
        val fieldValueMap = entityMapper.getFieldNameToValueMap(entity)

        logger.trace { fieldValueMap.toString() }

        return insert.usingGeneratedKeyColumns(entityMapper.idFieldName)
            .withTableName(collectionName)
            .executeAndReturnKey(fieldValueMap)
            .toLong()
    }
}
