package com.chadmarchand.kutna.persistence.demo

import com.chadmarchand.common.types.Id

enum class PhoneNumberType(val id: Id) {
    HOME(1L),
    WORK(2L),
    CELL(3L)
}
