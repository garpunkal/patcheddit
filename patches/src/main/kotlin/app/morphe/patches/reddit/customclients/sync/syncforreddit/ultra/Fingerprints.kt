package app.morphe.patches.reddit.customclients.sync.syncforreddit.ultra

import app.morphe.patcher.Fingerprint

// Fingerprint for the method that sets up the Sync Ultra menu item
internal val syncUltraMenuItemFingerprint = Fingerprint(
    strings = listOf(
        "SyncUltra",
        "syncUltra",
        "mSyncUltraRow",
        "ultraPremium",
        "onUpgradeClicked",
        "upgrade",
    ),
    custom = { _, classDef ->
        classDef.sourceFile?.contains("Main", ignoreCase = true) == true ||
            classDef.sourceFile?.contains("Drawer", ignoreCase = true) == true ||
            classDef.sourceFile?.contains("Menu", ignoreCase = true) == true
    }
)

// Fingerprint for the method that sets up the "Get Sync Ultra" sidemenu item
internal val getSyncUltraMenuItemFingerprint = Fingerprint(
    strings = listOf(
        "mUpgradeRow",
        "mSyncUltraRow",
        "onUpgradeClicked",
        "getSync",
        "ultraRow",
        "upgrade",
    ),
    custom = { _, classDef ->
        classDef.sourceFile?.contains("Main", ignoreCase = true) == true ||
            classDef.sourceFile?.contains("Drawer", ignoreCase = true) == true ||
            classDef.sourceFile?.contains("Menu", ignoreCase = true) == true
    }
)
