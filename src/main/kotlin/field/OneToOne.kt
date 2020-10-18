package com.chadmarchand.kutna.persistence.field

import com.chadmarchand.kutna.persistence.mapper.DEFAULT_ID_FIELD

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class OneToOne(
    val sourceFieldName: String = DEFAULT_ID_FIELD,
    val targetFieldName: String
)
