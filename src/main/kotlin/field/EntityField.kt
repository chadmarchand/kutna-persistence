package com.chadmarchand.kutna.persistence.field

import java.lang.reflect.Field
import java.lang.reflect.ParameterizedType

open class EntityField internal constructor(
    val field: Field
) {
    val fieldName: String = field.name
    private val accessor = EntityFieldValueAccessor(field)

    fun getValue(entity: Any): Any? = accessor.getValue(entity)

    fun setValue(entity: Any, value: Any?) = accessor.setValue(entity, value)

    fun getFieldElementType(): Class<*> {
        if (isListField()) {
            return getListElementType()
        }

        return field.type
    }

    fun isListField(): Boolean {
        try {
            getListElementType()
        } catch (e: ClassCastException) {
            return false
        }

        return true
    }

    private fun getListElementType(): Class<*> {
        val genericSuperclass = field.genericType as ParameterizedType
        return genericSuperclass.actualTypeArguments[0] as Class<*>
    }
}
