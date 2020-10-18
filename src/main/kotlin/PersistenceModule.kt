package com.chadmarchand.kutna.persistence

import com.chadmarchand.kutna.persistence.configuration.JdbcConfiguration
import com.chadmarchand.kutna.persistence.query.SqlQueryFactory
import org.koin.dsl.module
import org.koin.experimental.builder.single

val persistenceModule = module(createdAtStart = true) {
    single { JdbcConfiguration().jdbcTemplate() }
    single<SqlQueryFactory>()
}
