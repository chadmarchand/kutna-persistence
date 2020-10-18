package com.chadmarchand.kutna.persistence.operation

import com.chadmarchand.kutna.persistence.mapper.EntityMapper
import kotlin.reflect.KClass

class OperationFactory<T: Any> constructor(
    private var entityType: KClass<T>,
    private var tableName: String
) {
    private var entityMapper: EntityMapper<T> = EntityMapper(entityType)

    fun createInsertAndReturnKeyAction() = InsertAndReturnKeyOperation(tableName, entityType)

    fun createSelectListAction() = SelectListOperation(tableName, entityType)

    fun createSelectObjectAction() = SelectObjectOperation(tableName, entityType)

    fun createUpdateAction() = UpdateOperation(tableName, entityType)

    fun createPatchAction() = PatchOperation(tableName, entityMapper)

    fun createDeleteByIdAction() = DeleteByIdOperation(tableName, entityType)

    fun createDeleteByClausesAction() = DeleteByClausesOperation(tableName)
}
