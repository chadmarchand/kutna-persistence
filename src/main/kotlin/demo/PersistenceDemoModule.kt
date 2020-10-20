package com.chadmarchand.kutna.persistence.demo

import com.chadmarchand.kutna.persistence.repository.Repository
import com.chadmarchand.kutna.persistence.service.getRepositoryQualifier
import org.koin.dsl.module
import org.koin.experimental.builder.single

private val NAME_REPOSITORY_QUALIFIER = getRepositoryQualifier<Name>()
private val PHONE_NUMBER_REPOSITORY_QUALIFIER = getRepositoryQualifier<PhoneNumber>()
private val PERSON_REPOSITORY_QUALIFIER = getRepositoryQualifier<Person>()

val persistenceDemoModule = module(createdAtStart = true) {
    single(NAME_REPOSITORY_QUALIFIER) { Repository(Name::class )}
    single(PHONE_NUMBER_REPOSITORY_QUALIFIER ) { Repository(PhoneNumber::class )}
    single(PERSON_REPOSITORY_QUALIFIER) { Repository(Person::class) }

    single<PersonDataService>()
}
