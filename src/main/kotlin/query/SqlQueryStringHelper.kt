package com.chadmarchand.kutna.persistence.query

object SqlQueryStringHelper {

    fun generateSelectString(tableName: String, fieldLabels: List<String>, whereClauseKeys: Set<String>): String {
        val queryStrBuilder = StringBuilder("SELECT ")

        addFieldsToQuery(queryStrBuilder, fieldLabels, false)
        queryStrBuilder.append(" FROM ").append(tableName).append(" ")
        addWhereClausesToQuery(queryStrBuilder, whereClauseKeys)

        return queryStrBuilder.toString()
    }

    fun generateUpdateString(tableName: String, fieldLabels: List<String>, whereClauseKeys: Set<String>): String {
        val queryStrBuilder = StringBuilder("UPDATE $tableName SET ")

        addFieldsToQuery(queryStrBuilder, fieldLabels, true)
        queryStrBuilder.append(" ")
        addWhereClausesToQuery(queryStrBuilder, whereClauseKeys)

        return queryStrBuilder.toString()
    }

    fun generateDeleteString(tableName: String, whereClauseKeys: Set<String>): String {
        val queryStrBuilder = StringBuilder("DELETE FROM $tableName ")

        addWhereClausesToQuery(queryStrBuilder, whereClauseKeys)

        return queryStrBuilder.toString()
    }

    private fun addFieldsToQuery(
        queryStrBuilder: StringBuilder,
        fieldLabels: List<String>,
        includeQuestionMark: Boolean) {

        var first = true
        for (field in fieldLabels) {
            if (!first) {
                queryStrBuilder.append(", ")
            }

            queryStrBuilder.append(field)

            if (includeQuestionMark) {
                queryStrBuilder.append("=?")
            }

            first = false
        }
    }

    private fun addWhereClausesToQuery(queryStrBuilder: StringBuilder, whereClauseKeys: Set<String>) {
        var first = true
        if (!whereClauseKeys.isEmpty()) {
            queryStrBuilder.append("WHERE ")

            for (key in whereClauseKeys) {
                if (!first) {
                    queryStrBuilder.append(" AND ")
                }
                queryStrBuilder.append(key)
                queryStrBuilder.append("=?")
                first = false
            }
        }
    }
}
