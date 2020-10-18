package com.chadmarchand.kutna.persistence.query

import org.springframework.jdbc.core.JdbcTemplate

class DeleteByClausesQuery constructor(
    private val tableName: String,
    private val whereClauses: Map<String, Any>,
    private val jdbcTemplate: JdbcTemplate
) {

    fun execute() {
        val queryString = SqlQueryStringHelper.generateDeleteString(tableName, whereClauses.keys)

        val values = whereClauses.values.toTypedArray()
        jdbcTemplate.update(queryString, *values)
    }
}
