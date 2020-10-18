package com.chadmarchand.kutna.persistence.query

import org.springframework.jdbc.core.PreparedStatementCreator

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.SQLException
import java.sql.Types

class UpdatePreparedStatementCreator(
    updatedInfo: Map<String, Any?>,
    whereClauses: Map<String, String>,
    private val queryString: String
) : PreparedStatementCreator {

    private val updatedFieldValues: Array<Any?> = updatedInfo.values.toTypedArray()
    private val whereClauseValues: Array<Any> = whereClauses.values.toTypedArray()

    override fun createPreparedStatement(connection: Connection): PreparedStatement {
        try {
            val preparedStatement = connection.prepareStatement(queryString)

            setUpdatedFieldParameters(preparedStatement, 1)
            setWhereClauseParameters(preparedStatement, updatedFieldValues.size + 1)

            return preparedStatement
        } catch (e: SQLException) {
            throw PreparedStatementCreationException("Error creating prepared statement")
        }

    }

    @Throws(SQLException::class)
    private fun setUpdatedFieldParameters(statement: PreparedStatement, parameterIndex: Int) {
        var parameterIndex = parameterIndex
        for (i in updatedFieldValues.indices) {
            setParameter(statement, parameterIndex, updatedFieldValues[i])
            parameterIndex++
        }
    }

    @Throws(SQLException::class)
    private fun setWhereClauseParameters(statement: PreparedStatement, parameterIndex: Int) {
        var parameterIndex = parameterIndex
        for (i in whereClauseValues.indices) {
            setParameter(statement, parameterIndex, whereClauseValues[i])
            parameterIndex++
        }
    }

    @Throws(SQLException::class)
    private fun setParameter(statement: PreparedStatement, parameterIndex: Int, parameterValue: Any?) {
        if (parameterValue == null) {
            // This may cause issues for null fields that are not integers
            statement.setNull(parameterIndex, Types.INTEGER)
        } else {
            statement.setObject(parameterIndex, parameterValue)
        }
    }
}
