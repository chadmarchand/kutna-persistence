package com.chadmarchand.kutna.persistence.table

import com.chadmarchand.common.types.Id
import com.chadmarchand.kutna.persistence.operation.OperationFactory
import kotlin.reflect.KClass

class Table<T : Any> {
    private lateinit var operationFactory: OperationFactory<T>
    private lateinit var name: String
    private lateinit var entityType: KClass<T>

    fun initialize(entityType: KClass<T>, tableName: String) {
        this.name = tableName
        this.entityType = entityType
        this.operationFactory = createActionFactory()
    }

    fun addAndReturnKey(entity: T): Id {
        return operationFactory
            .createInsertAndReturnKeyAction()
            .execute(entity)
    }

    fun selectList(whereClauses: Map<String, Any>): List<T> {
        return operationFactory
            .createSelectListAction()
            .execute(whereClauses)
    }

    fun selectObject(whereClauses: Map<String, Any>): T {
        return operationFactory
            .createSelectObjectAction()
            .execute(whereClauses)
    }

    fun update(entity: T, whereClauses: Map<String, String>) {
        return operationFactory
            .createUpdateAction()
            .execute(entity, whereClauses)
    }

    fun patch(entityWithoutPatch: T, updatedInfo: Map<String, Any?>, whereClauses: Map<String, String>) {
        return operationFactory
            .createPatchAction()
            .execute(entityWithoutPatch, updatedInfo, whereClauses)
    }

    fun delete(id: Long) {
        return operationFactory
            .createDeleteByIdAction()
            .execute(id)
    }

    fun delete(whereClauses: Map<String, Any>) {
        return operationFactory
            .createDeleteByClausesAction()
            .execute(whereClauses)
    }

    private fun createActionFactory(): OperationFactory<T> {
        return OperationFactory(entityType, name)
    }
}
