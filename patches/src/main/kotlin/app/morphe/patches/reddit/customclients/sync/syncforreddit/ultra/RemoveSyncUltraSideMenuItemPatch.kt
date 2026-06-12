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
        getSyncUltraMenuItemFingerprint.matchAll().forEach { match ->
            val method = match.method
            val instructions = method.implementation?.instructions ?: return@forEach
            val indicesToRemove = mutableSetOf<Int>()

            // Remove a conservative window around each matched upgrade/sidemenu key setup.
            match.stringMatches
                .filter {
                    val value = it.string
                    value.contains("mUpgradeRow", ignoreCase = true) ||
                        value.contains("mSyncUltraRow", ignoreCase = true) ||
                        value.contains("onUpgradeClicked", ignoreCase = true) ||
                        value.contains("getSync", ignoreCase = true)
                }
                .forEach { stringMatch ->
                    val baseIndex = stringMatch.index
                    for (offset in -10..20) {
                        val idx = baseIndex + offset
                        if (idx in 0 until instructions.size) {
                            indicesToRemove.add(idx)
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
