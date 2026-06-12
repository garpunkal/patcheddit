package app.morphe.patches.reddit.customclients.sync.syncforreddit.ultra

import app.morphe.patcher.Fingerprint

// Candidate fingerprints for methods that set up Sync Ultra menu items.
internal val syncUltraMenuItemFingerprints = listOf(
    Fingerprint(
        strings = listOf("mSyncUltraRow")
    ),
    Fingerprint(
        strings = listOf("ultraPremium")
    ),
    Fingerprint(
        strings = listOf("SyncUltra")
    ),
    Fingerprint(
        strings = listOf("Sync Ultra")
    )
)

// Candidate fingerprints for methods that set up the "Get Sync Ultra" sidemenu item.
internal val getSyncUltraMenuItemFingerprints = listOf(
    Fingerprint(
        strings = listOf("mUpgradeRow")
    ),
    Fingerprint(
        strings = listOf("onUpgradeClicked")
    ),
    Fingerprint(
        strings = listOf("getSync")
    ),
    Fingerprint(
        strings = listOf("Get Sync Ultra")
    )
)
