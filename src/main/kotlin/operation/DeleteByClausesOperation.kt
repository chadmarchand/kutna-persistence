package com.chadmarchand.kutna.persistence.operation

import com.chadmarchand.kutna.persistence.query.SqlQueryFactory
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DeleteByClausesOperation constructor(
    private val collectionName: String
) : SqlOperation, KoinComponent {

    private val sqlQueryFactory: SqlQueryFactory by inject()

    fun execute(whereClauses: Map<String, Any>) {
        val query = sqlQueryFactory.createDeleteByClausesQuery(collectionName, whereClauses)
        query.execute()
    }
}
