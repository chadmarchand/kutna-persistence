package com.chadmarchand.kutna.persistence.operation

import com.chadmarchand.kutna.persistence.query.SqlQueryFactory
import org.koin.core.KoinComponent
import org.koin.core.inject
import kotlin.reflect.KClass

class SelectListOperation<T: Any> constructor(
    private val collectionName: String,
    private val entityType: KClass<T>
) : KoinComponent {
    private val sqlQueryFactory: SqlQueryFactory by inject()

    fun execute(whereClauses: Map<String, Any>): List<T> {
        val query = sqlQueryFactory.createSelectListQuery(collectionName, entityType, whereClauses)
        return query.execute()
    }
}
