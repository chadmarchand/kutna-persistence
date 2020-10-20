package com.chadmarchand.kutna.persistence.field

import com.chadmarchand.kutna.persistence.field.OneToMany
import com.chadmarchand.kutna.persistence.field.OneToOne
import com.chadmarchand.kutna.persistence.field.Persist
import com.chadmarchand.kutna.persistence.field.PrimaryId
import com.chadmarchand.kutna.persistence.mapper.EntityMapper
import java.lang.RuntimeException
import java.lang.reflect.Field

fun isPersistentField(field: Field) = getPersistenceAnnotation(field) != null

fun isIdField(field: Field) = getIdAnnotation(field) != null

fun isLinkedField(field: Field) = hasOneToManyAnnotation(field) || hasOneToOneAnnotation(field)

fun getLinkedFieldTargetFieldName(field: Field): String {
    if (hasOneToManyAnnotation(field)) {
        return getOneToManyAnnotation(field).targetFieldName
    } else if (hasOneToOneAnnotation(field)) {
        return getOneToOneAnnotation(field).targetFieldName
    }

    throw RuntimeException("Expected field to have a link relationship annotation")
}

fun <T: Any> getLinkedFieldTargetValue(entityMapper: EntityMapper<T>, field: Field, parentEntity: T): Any {
    val targetFieldName = getLinkedFieldTargetFieldName(field)
    return entityMapper.getFieldByName(targetFieldName).getValue(parentEntity)!!
}

fun <T: Any> getLinkedFieldSourceValue(entityMapper: EntityMapper<T>, field: Field, parentEntity: T): Any {
    val sourceFieldName = getLinkedFieldSourceFieldName(field)
    return entityMapper.getFieldByName(sourceFieldName).getValue(parentEntity)!!
}

fun getLinkedFieldSourceFieldName(field: Field): String {
    if (hasOneToManyAnnotation(field)) {
        return getOneToManyAnnotation(field).sourceFieldName
    } else if (hasOneToOneAnnotation(field)) {
        return getOneToOneAnnotation(field).sourceFieldName
    }

    throw RuntimeException("Expected field to have a link relationship annotation")
}

fun hasOneToManyAnnotation(field: Field) = getOneToManyAnnotation(field) != null

fun hasOneToOneAnnotation(field: Field) = getOneToOneAnnotation(field) != null

private fun getOneToManyAnnotation(field: Field) = field.getDeclaredAnnotation(OneToMany::class.java)

private fun getOneToOneAnnotation(field: Field) = field.getDeclaredAnnotation(OneToOne::class.java)

private fun getPersistenceAnnotation(field: Field) = field.getDeclaredAnnotation(Persist::class.java)

private fun getIdAnnotation(field: Field) = field.getDeclaredAnnotation<PrimaryId>(PrimaryId::class.java)
