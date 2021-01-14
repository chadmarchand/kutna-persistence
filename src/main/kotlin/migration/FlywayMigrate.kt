package com.chadmarchand.kutna.persistence.migration

import com.chadmarchand.kutna.persistence.configuration.getDataSource
import org.flywaydb.core.Flyway

fun flywayMigrate(): Unit {
    val flyway = Flyway.configure()
        .dataSource(getDataSource())

    val f = Flyway(flyway)
    f.clean()
    f.migrate()
}
