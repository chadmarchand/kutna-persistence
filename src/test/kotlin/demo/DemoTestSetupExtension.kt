package com.chadmarchand.kutna.persistence.demo

import com.chadmarchand.kutna.persistence.KutnaTestSetupExtensionPersistent
import com.chadmarchand.kutna.persistence.persistenceModule

class DemoTestSetupExtension : KutnaTestSetupExtensionPersistent(
    listOf(
        persistenceModule,
        persistenceDemoModule
    )
)
