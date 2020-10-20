package com.chadmarchand.kutna.persistence.field

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class OneToMany(
    val sourceFieldName: String = DEFAULT_ID_FIELD,
    val targetFieldName: String
)
