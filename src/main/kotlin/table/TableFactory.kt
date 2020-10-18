package com.chadmarchand.kutna.persistence.table

import kotlin.reflect.KClass

class TableFactory {
    companion object {
        fun <T : Any> create(entityType: KClass<T>, tableName: String): Table<T> {
            val table = Table<T>()
            table.initialize(entityType, tableName)

            return table
        }
    }
}
