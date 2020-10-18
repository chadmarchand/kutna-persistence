package com.chadmarchand.kutna.persistence.query

import org.springframework.dao.DataAccessException

class DataIntegrityException(
    msg: String? = "",
    e: Exception?
) : DataAccessException(msg, e)
