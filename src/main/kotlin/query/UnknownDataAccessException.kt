package com.chadmarchand.kutna.persistence.query

import org.springframework.dao.DataAccessException

class UnknownDataAccessException(
    msg: String,
    e: Throwable? = null
) : DataAccessException(msg, e)
