package com.chadmarchand.kutna.persistence.query

enum class SqlErrorCode(val code: Int) {
    FIELD_NOT_NULLABLE_ERROR_CODE(1048),
    DUPLICATE_KEY_ERROR_CODE(1062),
    DATA_INTEGRITY_ERROR_CODE(1452)
}
