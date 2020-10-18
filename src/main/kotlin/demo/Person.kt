package com.chadmarchand.kutna.persistence.demo

import com.chadmarchand.common.types.DEFAULT_ID
import com.chadmarchand.common.types.Id
import com.chadmarchand.kutna.persistence.field.OneToMany
import com.chadmarchand.kutna.persistence.field.OneToOne
import com.chadmarchand.kutna.persistence.field.Persist
import com.chadmarchand.kutna.persistence.field.PrimaryId

data class Person(
    @Persist
    @PrimaryId
    var id: Id = Id.DEFAULT_ID,

    @Persist
    @OneToOne(sourceFieldName = "id", targetFieldName = "personId")
    var name: Name? = null,

    @Persist
    @OneToMany(sourceFieldName = "id", targetFieldName = "personId")
    var phoneNumbers: List<PhoneNumber> = emptyList()
)
