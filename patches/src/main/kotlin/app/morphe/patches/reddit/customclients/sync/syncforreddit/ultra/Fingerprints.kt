package app.morphe.patches.reddit.customclients.sync.syncforreddit.ultra

import app.morphe.patcher.Fingerprint
import com.android.tools.smali.dexlib2.iface.ClassDef

private fun isLikelyUltraMenuClass(classDef: ClassDef): Boolean {
    val sourceFile = classDef.sourceFile ?: return false
    return sourceFile.contains("Main", ignoreCase = true) ||
        sourceFile.contains("Drawer", ignoreCase = true) ||
        sourceFile.contains("Menu", ignoreCase = true)
}

// Candidate fingerprints for methods that set up Sync Ultra menu items.
internal val syncUltraMenuItemFingerprints = listOf(
    Fingerprint(
        strings = listOf("mSyncUltraRow"),
        custom = { _, classDef -> isLikelyUltraMenuClass(classDef) }
    ),
    Fingerprint(
        strings = listOf("ultraPremium"),
        custom = { _, classDef -> isLikelyUltraMenuClass(classDef) }
    ),
    Fingerprint(
        strings = listOf("SyncUltra"),
        custom = { _, classDef -> isLikelyUltraMenuClass(classDef) }
    )
)

// Candidate fingerprints for methods that set up the "Get Sync Ultra" sidemenu item.
internal val getSyncUltraMenuItemFingerprints = listOf(
    Fingerprint(
        strings = listOf("mUpgradeRow"),
        custom = { _, classDef -> isLikelyUltraMenuClass(classDef) }
    ),
    Fingerprint(
        strings = listOf("onUpgradeClicked"),
        custom = { _, classDef -> isLikelyUltraMenuClass(classDef) }
    ),
    Fingerprint(
        strings = listOf("getSync"),
        custom = { _, classDef -> isLikelyUltraMenuClass(classDef) }
    )
)
