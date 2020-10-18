package com.chadmarchand.kutna.persistence

import com.chadmarchand.koin.KoinSetupExtension
import com.chadmarchand.kutna.persistence.configuration.getDataSource
import org.flywaydb.core.Flyway
import org.junit.jupiter.api.extension.ExtensionContext
import org.koin.core.module.Module

open class KutnaTestSetupExtensionPersistent constructor(
    koinModules: List<Module>
) : KoinSetupExtension(koinModules) {
    override fun beforeEach(context: ExtensionContext) {
        super.beforeEach(context)
        setupDatabase()
    }

    private fun setupDatabase() {
        val flyway = Flyway.configure()
            .dataSource(getDataSource())

        val f = Flyway(flyway)
        f.clean()
        f.migrate()
    }
}
