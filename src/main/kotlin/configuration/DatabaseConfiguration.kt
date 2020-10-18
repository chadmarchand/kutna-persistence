package com.chadmarchand.kutna.persistence.configuration

import org.springframework.boot.jdbc.DataSourceBuilder
import javax.sql.DataSource

private const val IN_MEMORY_DATABASE_URL = "jdbc:h2:mem:testdb"
private const val IN_MEMORY_DRIVER_CLASS_NAME = "org.h2.Driver"
private const val MYSQL_JDBC_DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver"
private const val USE_TEST_DB_ENVIRONMENT_VARIABLE = "USE_TEST_DB"
private const val DATABASE_URL_PROPERTY = "DB_URL"
private const val DATABASE_USERNAME_PROPERTY = "DB_USERNAME"
private const val DATABASE_PASSWORD_PROPERTY = "DB_PASSWORD"

fun getDataSource(): DataSource {
    if (isUsingTestDatabase()) {
        return getInMemoryDatabase()
    }
    return getPersistentDatabase()
}

fun getPersistentDatabase(): DataSource {
    return DataSourceBuilder.create()
        .driverClassName(MYSQL_JDBC_DRIVER_CLASS_NAME)
        .url(System.getenv(DATABASE_URL_PROPERTY))
        .username(System.getenv(DATABASE_USERNAME_PROPERTY))
        .password(System.getenv(DATABASE_PASSWORD_PROPERTY))
        .build()
}

fun getInMemoryDatabase(): DataSource {
    return DataSourceBuilder.create()
        .driverClassName(IN_MEMORY_DRIVER_CLASS_NAME)
        .url(IN_MEMORY_DATABASE_URL)
        .username(System.getenv(DATABASE_USERNAME_PROPERTY))
        .password(System.getenv(DATABASE_PASSWORD_PROPERTY))
        .build()
}

private fun isUsingTestDatabase(): Boolean {
    return System.getenv(USE_TEST_DB_ENVIRONMENT_VARIABLE) != null
}
