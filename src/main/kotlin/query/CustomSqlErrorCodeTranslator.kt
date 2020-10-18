package com.chadmarchand.kutna.persistence.query

import org.springframework.dao.DataAccessException
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator

import java.sql.SQLException

class CustomSqlErrorCodeTranslator : SQLErrorCodeSQLExceptionTranslator() {
    override fun customTranslate(task: String?, sql: String?, sqlException: SQLException): DataAccessException? {
        return if (sqlException.errorCode == SqlErrorCode.DATA_INTEGRITY_ERROR_CODE.code
            || sqlException.errorCode == SqlErrorCode.FIELD_NOT_NULLABLE_ERROR_CODE.code
            || sqlException.errorCode == SqlErrorCode.DUPLICATE_KEY_ERROR_CODE.code) {

            DataIntegrityException(sqlException.message, sqlException)
        } else UnknownDataAccessException(
            "Unknown SQL error occured with the following message: " + sqlException.message,
            sqlException
        )
    }
}
