package com.chadmarchand.kutna.persistence.operation

import com.chadmarchand.kutna.persistence.query.SqlQueryFactory
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.reflect.KClass

class DeleteByIdOperation<T: Any> constructor(
    private val collectionName: String,
    private val entityType: KClass<T>
) : SqlOperation, KoinComponent {

    private val sqlQueryFactory: SqlQueryFactory by inject()

    fun execute(id: Long) {
        val query = sqlQueryFactory.createDeleteByIdQuery(collectionName, entityType)
        query.execute(id)
    }
}
