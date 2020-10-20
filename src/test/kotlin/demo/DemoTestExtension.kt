package com.chadmarchand.kutna.persistence.demo

import com.chadmarchand.kutna.persistence.KutnaTestSetupExtensionPersistent
import com.chadmarchand.kutna.persistence.persistenceModule

class DemoTestExtension : KutnaTestSetupExtensionPersistent(
    listOf(
        persistenceModule,
        persistenceDemoModule
    )
)
