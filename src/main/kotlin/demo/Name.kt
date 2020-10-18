package com.chadmarchand.kutna.persistence.demo

import com.chadmarchand.common.types.DEFAULT_ID
import com.chadmarchand.common.types.Id
import com.chadmarchand.kutna.persistence.field.Persist
import com.chadmarchand.kutna.persistence.field.PrimaryId

data class Name(
    @PrimaryId
    @Persist
    var id: Id = Id.DEFAULT_ID,

    @Persist
    var personId: Id = Id.DEFAULT_ID,

    @Persist
    var givenName: String = "",

    @Persist
    var familyName: String = ""
)
