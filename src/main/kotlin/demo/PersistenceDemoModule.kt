package com.chadmarchand.kutna.persistence.demo

import com.chadmarchand.kutna.persistence.repository.Repository
import com.chadmarchand.kutna.persistence.service.getRepositoryQualifier
import org.koin.dsl.module
import org.koin.experimental.builder.single

private val PERSON_REPOSITORY_QUALIFIER = getRepositoryQualifier<Person>()

val persistenceDemoModule = module(createdAtStart = true) {
    single(PERSON_REPOSITORY_QUALIFIER) { Repository(Person::class) }
    single<PersonDataService>()
}
