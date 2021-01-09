package com.chadmarchand.kutna.persistence.operation

import com.chadmarchand.kutna.persistence.query.SqlQueryFactory
import com.chadmarchand.kutna.persistence.mapper.EntityMapper
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PatchOperation<T : Any>(
    private val collectionName: String,
    private val entityMapper: EntityMapper<T>
) : SqlOperation, KoinComponent {

    private val sqlQueryFactory: SqlQueryFactory by inject()

    fun execute(entityWithoutPatch: T, updatedInfo: Map<String, Any?>, whereClauses: Map<String, String>) {
        updateEntityWithPatch(entityWithoutPatch, updatedInfo)

        val query = sqlQueryFactory.createPatchQuery<T>(collectionName, whereClauses)
        query.execute(updatedInfo)
    }

    private fun updateEntityWithPatch(entity: T, updatedInfo: Map<String, Any?>) {
        for (field in entityMapper.getEntityFields(false)) {
            if (updatedInfo.containsKey(field.fieldName)) {
                field.setValue(entity, updatedInfo[field.fieldName])
            }
        }
    }
}
