package com.chadmarchand.kutna.persistence.operation

import com.chadmarchand.common.types.Id
import com.chadmarchand.kutna.persistence.query.SqlQueryFactory
import org.koin.core.KoinComponent
import org.koin.core.inject
import kotlin.reflect.KClass

class InsertAndReturnKeyOperation<T: Any> constructor(
    private val collectionName: String,
    private val entityType: KClass<T>
) : SqlOperation, KoinComponent {

    private val sqlQueryFactory: SqlQueryFactory by inject()

    fun execute(entity: T): Id {
        val query = sqlQueryFactory.createInsertAndReturnKeyQuery(collectionName, entityType)
        return query.execute(entity)
    }
}
