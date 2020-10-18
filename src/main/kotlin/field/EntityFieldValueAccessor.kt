package com.chadmarchand.kutna.persistence.field

import java.lang.reflect.Field

class EntityFieldValueAccessor constructor(
    private val field: Field
) {
    fun getValue(entity: Any): Any? {
        try {
            field.isAccessible = true
            return field.get(entity)
        } catch (e: IllegalAccessException) {
            throw EntityFieldAccessException("Could not get value of entity field ${field.name} for entity $entity", e)
        }
    }

    fun setValue(entity: Any, value: Any?) {
        try {
            field.isAccessible = true
            field.set(entity, value)
        } catch (e: Exception) {
            throw EntityFieldAccessException("Could not set value of entity field ${field.name} for entity $entity", e)
        }
    }
}
