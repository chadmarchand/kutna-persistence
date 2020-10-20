package com.chadmarchand.kutna.persistence.mapper

import com.chadmarchand.kutna.persistence.field.*
import java.lang.RuntimeException
import java.util.*
import java.util.stream.Collectors
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaField

class EntityMapper<T: Any>(
    private val entityType: KClass<T>
) {
    private val fields: List<EntityField>

    val fieldNames: List<String>
        get() = getFieldNames(false)

    val idFieldName: String
        get() = fields.stream()
            .filter { isIdField(it.field) }
            .findFirst()
            .map { it.fieldName }
            .orElseThrow { EntityFieldAccessException("ID field not found for type $entityType") }

    val linkedFields: List<EntityField>
        get() = fields.stream()
            .filter { isLinkedField(it.field) }
            .collect(Collectors.toList())

    init {
        this.fields = generateMappedFields()
    }

    @JvmOverloads
    fun getFieldNameToValueMap(entity: T, includeLinked: Boolean = false): MutableMap<String, Any> {
        val map = HashMap<String, Any>()
        val mappedFields = getEntityFields(includeLinked)

        for (field in mappedFields) {
            val value = field.getValue(entity)

            if (value != null) {
                map[field.fieldName] = value
            }
        }

        return map
    }

    fun setEntityId(entity: T, id: Long?) {
        val idField = getEntityFieldByName(idFieldName)
        idField.setValue(entity, id)
    }

    fun getEntityId(entity: T): Long? {
        val idField = getEntityFieldByName(idFieldName)
        val idFieldValue = idField.getValue(entity)
        if (idFieldValue is Long) {
            return idField.getValue(entity) as Long?
        }

        throw EntityIdFormatException("Expected model ID field '$idField' valueAsString to be a Long - was '$idFieldValue'")
    }

    fun getFieldByName(name: String): EntityField {
        return fields.stream()
            .filter { field -> field.fieldName == name }
            .findFirst()
            .orElseThrow {
                RuntimeException("Could not get source field name for linked field")
            }
    }

    fun getEntityFields(includeLinked: Boolean?): List<EntityField> {
        return fields.stream()
            .filter { field -> includeLinked!! || !isLinkedField(field.field) }
            .collect(Collectors.toList())
    }

    private fun getFieldNames(includeLinked: Boolean?): List<String> {
        return getEntityFields(includeLinked)
            .stream()
            .map { it.fieldName }
            .collect(Collectors.toList())
    }

    private fun getEntityFieldByName(fieldName: String): EntityField {
        return fields.stream()
            .filter { field -> field.fieldName == fieldName }
            .findFirst()
            .orElseThrow { EntityFieldAccessException("Entity field with name $fieldName was not found") }
    }

    private fun generateMappedFields(): List<EntityField> {
        val mappedFields = ArrayList<EntityField>()

        entityType.memberProperties.forEach { field ->
            if (isPersistentField(field.javaField!!)) {
                mappedFields.add(EntityField(field.javaField!!))
            }
        }

        return mappedFields
    }
}
