package com.chadmarchand.kutna.persistence.field

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class Persist(
    val value: String = ""
)
