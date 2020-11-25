package com.chadmarchand.kutna.persistence.demo

import com.chadmarchand.kutna.common.types.DEFAULT_ID
import com.chadmarchand.kutna.common.types.Id
import com.chadmarchand.kutna.persistence.field.Persist
import com.chadmarchand.kutna.persistence.field.PrimaryId

data class PhoneNumber(
    @PrimaryId
    @Persist
    var id: Id = Id.DEFAULT_ID,

    @Persist
    var personId: Id = Id.DEFAULT_ID,

    @Persist
    var typeId: Id = PhoneNumberType.HOME.id,

    @Persist
    var phoneNumber: String = ""
)