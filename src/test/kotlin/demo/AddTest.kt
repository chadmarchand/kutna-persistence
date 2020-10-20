package com.chadmarchand.kutna.persistence.demo

import org.assertj.core.api.AssertionsForClassTypes
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.koin.core.inject
import org.koin.test.KoinTest

@ExtendWith(DemoTestExtension::class)
class AddTest : KoinTest {
    private val personDataService: PersonDataService by inject()

    // @todo non-linked and linked...

    @Test
    fun add_withValidData_returnsCreatedEntityWithId() {
        val addedPerson = createAndAddPerson()

        val retrievedPerson = personDataService.get(addedPerson.id)
        AssertionsForClassTypes.assertThat(retrievedPerson).isNotNull
    }

    private fun createAndAddPerson(): Person {
        val name = Name(personId = 1, givenName = "Alfred", familyName = "Alfredsson")
        val phoneNumbers = listOf(
            PhoneNumber(typeId = PhoneNumberType.CELL.id, phoneNumber = "1-555-1111"),
            PhoneNumber(typeId = PhoneNumberType.HOME.id, phoneNumber = "1-555-1112")
        )
        val person = Person(name = name, phoneNumbers = phoneNumbers, isEmployed = true)
        return personDataService.add(person)
    }
}
