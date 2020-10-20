package com.chadmarchand.kutna.persistence.repository

import com.chadmarchand.kutna.persistence.field.EntityField
import com.chadmarchand.kutna.persistence.field.getLinkedFieldSourceValue
import com.chadmarchand.kutna.persistence.field.getLinkedFieldTargetFieldName
import com.chadmarchand.kutna.persistence.field.hasOneToManyAnnotation
import com.chadmarchand.kutna.persistence.service.getRepositoryQualifierFromEntity
import org.koin.core.KoinComponent
import org.koin.core.inject
import kotlin.reflect.KClass

open class LinkedRepository<T: Any> constructor(
    entityType: KClass<T>,
    private val linkedRepositories: List<Repository<*>> = emptyList()
) : KoinComponent, Repository<T>(entityType) {

    override val isLinked: Boolean
        get() = entityMapper.linkedFields.isNotEmpty()

    override fun fillEntitiesWithLinkedData(entities: List<T>) {
        entities.forEach{ this.fillEntityWithLinkedData(it) }
    }

    override fun fillEntityWithLinkedData(entity: T) {
        val linkedFields = entityMapper.linkedFields
        linkedFields.forEach { linkedField ->
            val linkedRepository = getLinkedRepositoryForField(linkedField)
            if (hasOneToManyAnnotation(linkedField.field)) {
                val linkedEntities = getLinkedEntities(linkedRepository, linkedField, entity)
                fillEntityFieldWithValue(entity, linkedField, linkedEntities)
            } else {
                val linkedEntity = getLinkedEntity(linkedRepository, linkedField, entity)
                fillEntityFieldWithValue(entity, linkedField, linkedEntity)
            }
        }
    }

    override fun updateLinkedEntities(parentEntity: T) {
        entityMapper.linkedFields.forEach { linkedField ->
            val linkedRepository = getLinkedRepositoryForField(linkedField)
            linkedRepository.updateListAndDeleteAbsent(
                linkedField.getValue(parentEntity) as List<Nothing>,
                getLinkedFieldTargetFieldName(linkedField.field),
                getLinkedFieldSourceValue(entityMapper, linkedField.field, parentEntity)
            )
        }
    }

    override fun deleteLinkedEntities(parentEntity: T) {
        entityMapper.linkedFields.forEach { linkedField ->
            val linkedRepository = getLinkedRepositoryForField(linkedField)
            linkedRepository.delete(
                mapOf(
                    Pair(
                        getLinkedFieldTargetFieldName(linkedField.field),
                        getLinkedFieldSourceValue(entityMapper, linkedField.field, parentEntity)
                    )
                )
            )
        }
    }

    override fun deleteLinkedEntities(parentEntities: List<T>) {
        parentEntities.forEach{ this.deleteLinkedEntities(it) }
    }

    private fun getLinkedRepositoryForField(field: EntityField): Repository<*> {
        val fieldEntityType = field.getFieldElementType()

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

    private fun <L: Any> getLinkedEntity(linkedRepository: Repository<L>, linkedField: EntityField, parentEntity: T): L {
        return getLinkedEntities(linkedRepository, linkedField, parentEntity).first()
    }

    private fun <L> fillEntityFieldWithValue(entity: T, field: EntityField, linkedEntities: L) {
        field.setValue(entity, linkedEntities)
    }
}
