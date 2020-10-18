package com.chadmarchand.kutna.persistence.repository

import com.chadmarchand.common.types.Id
import com.chadmarchand.kutna.persistence.repository.action.*
import com.chadmarchand.kutna.persistence.table.Table
import com.chadmarchand.kutna.persistence.table.TableFactory
import com.chadmarchand.kutna.persistence.mapper.EntityMapper
import java.util.*
import kotlin.reflect.KClass

open class Repository<T: Any> constructor(
    val entityType: KClass<T>
) {
    private val tableName = entityType.simpleName!!
    val table: Table<T> = TableFactory.create(entityType, tableName)
    val entityMapper: EntityMapper<T> = EntityMapper(entityType)
    open val isLinked: Boolean = false

    fun getAll(): List<T> {
        return GetListAction(this)
            .execute()
    }

    open fun addAndSetKey(entity: T, entityId: Id?): T {
        return AddAndSetKeyAction(this)
            .execute(entity)
    }

    fun get(params: Map<String, Any>): List<T> {
        return GetListAction(this)
            .addConditions(params)
            .execute()
    }

    fun getFirst(params: Map<String, Any>): Optional<T> {
        return get(params)
            .stream()
            .findFirst()
    }

    fun get(id: Long): T {
        return GetByIdAction(this)
            .execute(id)
    }

    fun getRefreshedEntity(entity: T): T {
        return get(entityMapper.getEntityId(entity)!!)
    }

    fun update(entity: T) {
        return UpdateAction(this)
            .execute(entity)
    }

    fun patch(updatedInfo: Map<String, Any?>, id: Long): T {
        return PatchAction(this)
            .execute(updatedInfo, id)
    }

    fun patch(id: Long, updatedInfo: Map<String, Any?>, whereClauses: Map<String, String>): T {
        return PatchAction(this)
            .execute(id, updatedInfo, whereClauses)
    }

    fun patch(id: Long, partialEntityJson: String): T {
        return PatchAction(this)
            .execute(id, partialEntityJson)
    }

    fun delete(id: Long) {
        return DeleteAction(this)
            .execute(id)
    }

    fun delete(entity: T) {
        return DeleteAction(this)
            .execute(entity)
    }

    fun delete(whereClauses: Map<String, Any>) {
        return DeleteAction(this)
            .execute(whereClauses)
    }

    // Update list of entities and delete entities that are not in the list and match the given key
    internal fun updateListAndDeleteAbsent(updatedEntities: List<T>, linkKeyName: String, linkKeyValue: Any) {
        val nonUpdatedEntities = get(mapOf(Pair(linkKeyName, linkKeyValue))).toMutableList()

        updatedEntities.forEach { updatedEntity ->
            update(updatedEntity)
            nonUpdatedEntities.removeIf{ entity -> entityMapper.getEntityId(entity) == entityMapper.getEntityId(updatedEntity) }
        }

        nonUpdatedEntities.forEach{ this.delete(it) }
    }

    internal open fun fillEntitiesWithLinkedData(entities: List<T>) {
        throw NotImplementedError()
    }

    internal open fun fillEntityWithLinkedData(entity: T) {
        throw NotImplementedError()
    }

    internal open fun updateLinkedEntities(parentEntity: T) {
        throw NotImplementedError()
    }

    internal open fun deleteLinkedEntities(parentEntity: T) {
        throw NotImplementedError()
    }

    internal open fun deleteLinkedEntities(parentEntities: List<T>) {
        throw NotImplementedError()
    }
}
