package app.morphe.patches.reddit.customclients.sync.syncforreddit.ultra

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.reddit.customclients.sync.SyncForRedditCompatible

@Suppress("unused")
val removeSyncUltraSideMenuItemPatch = bytecodePatch(
    name = "Remove 'Get Sync Ultra' sidemenu item",
    description = "Removes the 'Get Sync Ultra' item from the navigation sidemenu.",
) {
    compatibleWith(*SyncForRedditCompatible)

    execute {
        getSyncUltraMenuItemFingerprints.forEach { fingerprint ->
            val method = fingerprint.methodOrNull ?: return@forEach
            val instructions = method.implementation?.instructions ?: return@forEach
            val indicesToRemove = mutableSetOf<Int>()

            // Scan the method for known upgrade row/menu markers and remove nearby setup instructions.
            instructions.forEachIndexed { index, instruction ->
                val value = instruction.toString()
                if (
                    value.contains("mUpgradeRow", ignoreCase = true) ||
                    value.contains("mSyncUltraRow", ignoreCase = true) ||
                    value.contains("onUpgradeClicked", ignoreCase = true) ||
                    value.contains("getSync", ignoreCase = true)
                ) {
                    for (offset in -10..20) {
                        val idx = index + offset
                        if (idx in 0 until instructions.size) {
                            indicesToRemove.add(idx)
                        }
                    }
                }
            }

            indicesToRemove
                .sortedDescending()
                .forEach { index ->
                    if (index < instructions.size) {
                        try {
                            instructions.removeAt(index)
                        } catch (_: Exception) {
                            // Skip invalid removals and continue patching remaining matches.
                        }
                    }
                }
        }
    }
}
