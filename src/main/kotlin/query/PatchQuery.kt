package com.chadmarchand.kutna.persistence.query

import org.springframework.jdbc.core.JdbcTemplate
import java.util.*

class PatchQuery constructor(
    private val collectionName: String,
    private val whereClauses: Map<String, String>,
    private val jdbcTemplate: JdbcTemplate
) {

    fun execute(updatedInfo: Map<String, Any?>) {
        val keys = updatedInfo.keys.toTypedArray()
        val keysAsStrings = Arrays.copyOf<String, Any?>(keys, keys.size, Array<String>::class.java)
        val keyList = Arrays.asList(*keysAsStrings)

        val queryString = SqlQueryStringHelper.generateUpdateString(collectionName, keyList, whereClauses.keys)

        val preparedStatementCreator = UpdatePreparedStatementCreator(updatedInfo, whereClauses, queryString)
        jdbcTemplate.update(preparedStatementCreator)
    }
}
