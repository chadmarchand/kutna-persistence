package com.chadmarchand.kutna.persistence.demo

import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.koin.core.inject
import org.koin.test.KoinTest

@ExtendWith(DemoTestSetupExtension::class)
class GetByIdTest : KoinTest {
    private val personDataService: PersonDataService by inject()

    @Test
    fun canAddPerson() {
        val name = Name(personId = 1, givenName = "Alfred", familyName = "Alfredsson")
        val phoneNumbers = listOf(
            PhoneNumber(type = PhoneNumberType.CELL, phoneNumber = "1-555-1111"),
            PhoneNumber(type = PhoneNumberType.HOME, phoneNumber = "1-555-1112")
        )
        val person = Person(name = name, phoneNumbers = phoneNumbers, isEmployed = true)
        val addedPerson = personDataService.add(person)

        val retrievedPerson = personDataService.get(addedPerson.id)
        assertThat(retrievedPerson).isNotNull
    }
}
