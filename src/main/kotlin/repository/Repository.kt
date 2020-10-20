package com.chadmarchand.kutna.persistence.repository

import com.chadmarchand.common.types.Id
import com.chadmarchand.kutna.persistence.field.*
import com.chadmarchand.kutna.persistence.repository.action.*
import com.chadmarchand.kutna.persistence.table.Table
import com.chadmarchand.kutna.persistence.table.TableFactory
import com.chadmarchand.kutna.persistence.mapper.EntityMapper
import com.chadmarchand.kutna.persistence.service.getRepositoryQualifierFromEntity
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.*
import kotlin.reflect.KClass

open class Repository<T: Any> constructor(
    val entityType: KClass<T>
) : KoinComponent {
    private val tableName = entityType.simpleName!!
    val table: Table<T> = TableFactory.create(entityType, tableName)
    val entityMapper: EntityMapper<T> = EntityMapper(entityType)
    open val isLinked: Boolean
        get() = entityMapper.linkedFields.isNotEmpty()

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
        val linkedFields = entityMapper.linkedFields
        linkedFields.forEach { linkedField ->
            val linkedRepository = getLinkedRepositoryForField<Any>(linkedField)
            if (hasOneToManyAnnotation(linkedField.field)) {
                val linkedEntities = getLinkedEntities(linkedRepository, linkedField, entity)
                fillEntityFieldWithValue(entity, linkedField, linkedEntities)
            } else {
                val linkedEntity = getLinkedEntity(linkedRepository, linkedField, entity)
                if (linkedEntity != null) {
                    fillEntityFieldWithValue(entity, linkedField, linkedEntity)
                }
            }
        }
    }

    internal open fun updateLinkedEntities(parentEntity: T) {
        entityMapper.linkedFields.forEach { linkedField ->
            val linkedRepository = getLinkedRepositoryForField<Any>(linkedField)
            if (linkedField.isListField()) {
                linkedRepository.updateListAndDeleteAbsent(
                    linkedField.getValue(parentEntity) as List<Nothing>,
                    getLinkedFieldTargetFieldName(linkedField.field),
                    getLinkedFieldSourceValue(entityMapper, linkedField.field, parentEntity)
                )
            } else {
                linkedRepository.update(linkedField.getValue(parentEntity) as Any)
            }
        }
    }

    internal open fun deleteLinkedEntities(parentEntity: T) {
        throw NotImplementedError()
    }

    internal open fun deleteLinkedEntities(parentEntities: List<T>) {
        throw NotImplementedError()
    }

    private fun <E: Any> getLinkedRepositoryForField(field: EntityField): Repository<E> {
        val fieldEntityType = field.getFieldElementType() as Class<E>
        return getLinkedRepo(fieldEntityType)
    }

    private fun <E: Any> getLinkedRepo(entityType: Class<E>): Repository<E> {
        val linkedRepository: Repository<E> by inject(getRepositoryQualifierFromEntity(entityType))
        return linkedRepository
    }

    private fun <L: Any> getLinkedEntities(linkedRepository: Repository<L>, linkedField: EntityField, parentEntity: T): List<L> {
        return linkedRepository.get(
            mapOf(
                Pair(
                    getLinkedFieldTargetFieldName(linkedField.field),
                    getLinkedFieldSourceValue(entityMapper, linkedField.field, parentEntity)
                )
            )
        )
    }

    private fun <L: Any> getLinkedEntity(linkedRepository: Repository<L>, linkedField: EntityField, parentEntity: T): L? {
        return getLinkedEntities(linkedRepository, linkedField, parentEntity).firstOrNull()
    }

    private fun <L> fillEntityFieldWithValue(entity: T, field: EntityField, linkedEntities: L) {
        field.setValue(entity, linkedEntities)
    }
}
