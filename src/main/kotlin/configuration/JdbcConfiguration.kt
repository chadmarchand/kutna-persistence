package com.chadmarchand.kutna.persistence.configuration

import com.chadmarchand.kutna.persistence.query.CustomSqlErrorCodeTranslator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.core.JdbcTemplate
import java.sql.Connection
import java.sql.SQLException
import javax.sql.DataSource

@Configuration
open class JdbcConfiguration {
    @Bean
    @Primary
    open fun jdbcTemplate(): JdbcTemplate {
        val dataSource = getDataSource()
        return createJdbcTemplate(dataSource)
    }

    @Bean
    @Throws(SQLException::class)
    open fun connection(): Connection {
        val dataSource = getDataSource()
        return dataSource.connection
    }

    private fun createJdbcTemplate(dataSource: DataSource): JdbcTemplate {
        val jdbcTemplate = JdbcTemplate(dataSource)
        jdbcTemplate.exceptionTranslator = CustomSqlErrorCodeTranslator()

        return jdbcTemplate
    }
}
