package com.chadmarchand.kutna.persistence.operation

import com.chadmarchand.kutna.persistence.query.SqlQueryFactory
import org.koin.core.KoinComponent
import org.koin.core.inject
import kotlin.reflect.KClass

class UpdateOperation<T: Any> constructor(
    private val collectionName: String,
    private val entityType: KClass<T>
) : SqlOperation, KoinComponent {

    private val sqlQueryFactory: SqlQueryFactory by inject()

    fun execute(entity: T, whereClauses: Map<String, String>) {
        if (whereClauses.isEmpty()) {
            throw NoCriteriaUpdateException("Cannot update without where clauses")
        }

        val query = sqlQueryFactory.createUpdateQuery(collectionName, entityType, whereClauses)
        query.execute(entity)
    }
}
