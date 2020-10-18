package com.chadmarchand.kutna.persistence.mapper

import com.chadmarchand.kutna.persistence.field.EntityFieldAccessException
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet
import java.sql.SQLException
import kotlin.reflect.KClass

class SqlRowToEntityMapper<T: Any>(
    private val entityType: KClass<T>,
    private val entityMapper: EntityMapper<T>
) : RowMapper<T> {

    @Throws(SQLException::class)
    override fun mapRow(rs: ResultSet, rowNum: Int): T? {
        try {
            val entity = entityType.java.getConstructor().newInstance()

            for (field in entityMapper.getEntityFields(false)) {
                field.setValue(entity!!, rs.getObject(field.fieldName))
            }

            return entity
        } catch (e: Exception) {
            throw EntityFieldAccessException(e.message, e)
        }
    }
}
