package com.chadmarchand.kutna.persistence.query

import com.chadmarchand.kutna.persistence.mapper.EntityMapper
import org.springframework.jdbc.core.JdbcTemplate
import java.util.*
import kotlin.reflect.KClass

class UpdateQuery<T: Any> constructor(
    entityType: KClass<T>,
    private val collectionName: String,
    private val whereClauses: Map<String, Any>,
    private val jdbcTemplate: JdbcTemplate
) {

    private val entityMapper = EntityMapper(entityType)

    fun execute(entity: T) {
        val entityMap = entityMapper.getFieldNameToValueMap(entity)
        entityMap.remove(entityMapper.idFieldName)

        val queryString = createUpdateQueryString(entityMap)
        val valuesArray = getWhereClauseValues(entityMap)
        jdbcTemplate.update(queryString, *valuesArray)
    }

    private fun createUpdateQueryString(entityMap: Map<String, Any>): String {
        val keys = entityMap.keys.toTypedArray()
        val keysAsStrings = Arrays.copyOf<String, Any>(keys, keys.size, Array<String>::class.java)
        val keyList = Arrays.asList(*keysAsStrings)

        return SqlQueryStringHelper.generateUpdateString(collectionName, keyList, whereClauses.keys)
    }

    private fun getWhereClauseValues(entityMap: Map<String, Any>): Array<Any> {
        val values = entityMap.values.toTypedArray()
        val valuesAsList = ArrayList(Arrays.asList(*values))
        valuesAsList.addAll(whereClauses.values)

        return valuesAsList.toTypedArray()
    }
}
