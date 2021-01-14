package com.chadmarchand.kutna.persistence

import com.chadmarchand.kutna.common.koin.KutnaExtension
import com.chadmarchand.kutna.persistence.migration.flywayMigrate
import org.junit.jupiter.api.extension.ExtensionContext
import org.koin.core.module.Module

open class KutnaTestSetupExtensionPersistent constructor(
    koinModules: List<Module>
) : KutnaExtension(koinModules) {
    override fun beforeEach(context: ExtensionContext) {
        super.beforeEach(context)
        setupDatabase()
    }

    private fun setupDatabase() {
        flywayMigrate()
    }
}
