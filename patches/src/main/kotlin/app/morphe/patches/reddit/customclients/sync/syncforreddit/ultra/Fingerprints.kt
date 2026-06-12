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
    ),
    custom = { method, _ ->
        // Target methods that set up the Sync Ultra menu item
        true // Accept any method containing Sync Ultra related strings
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
    ),
    custom = { method, _ ->
        // Target methods that set up the sidemenu items
        true // Accept any method containing upgrade/sidemenu row setup
    }
)
