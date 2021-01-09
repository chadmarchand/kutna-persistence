package com.chadmarchand.kutna.persistence.operation

import com.chadmarchand.kutna.persistence.query.SqlQueryFactory
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.reflect.KClass

class SelectObjectOperation<T: Any> constructor(
    private val collectionName: String,
    private val entityType: KClass<T>
) : KoinComponent {
    private val sqlQueryFactory: SqlQueryFactory by inject()

    fun execute(whereClauses: Map<String, Any>): T {
        val query = sqlQueryFactory.createSelectObjectQuery(collectionName, entityType, whereClauses)
        return query.execute()
    }
}
