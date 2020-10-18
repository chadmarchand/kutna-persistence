package com.chadmarchand.kutna.persistence.service

class ServiceOperations(
    internal val isGetEnabled: Boolean,
    internal val isPostEnabled: Boolean,
    internal val isUpdateEnabled: Boolean,
    internal val isPatchEnabled: Boolean,
    internal val isDeleteEnabled: Boolean
) {

    companion object {
        val GET_ONLY = ServiceOperations(
            true,
            false,
            false,
            false,
            false)
        val ALL_EXCEPT_PATCH = ServiceOperations(
            true,
            true,
            true,
            false,
            true
        )
        val ALL = ServiceOperations(
            true,
            true,
            true,
            true,
            true
        )
    }
}
