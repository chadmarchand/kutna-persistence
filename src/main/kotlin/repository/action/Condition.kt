package com.chadmarchand.kutna.persistence.repository.action

import kotlin.reflect.KMutableProperty1

class Condition {
    var fieldName: String = ""
    var conditionValue: Any

    constructor(
        property: KMutableProperty1<*, *>,
        conditionValue: Any
    ) {
        this.fieldName = property.name
        this.conditionValue = conditionValue
    }

    constructor(
        fieldName: String,
        conditionValue: Any
    ) {
        this.fieldName = fieldName
        this.conditionValue = conditionValue
    }
}
