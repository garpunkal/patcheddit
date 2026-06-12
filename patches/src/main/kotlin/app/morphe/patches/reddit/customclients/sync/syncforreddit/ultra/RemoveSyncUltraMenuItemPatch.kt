package app.morphe.patches.reddit.customclients.sync.syncforreddit.ultra

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.reddit.customclients.sync.SyncForRedditCompatible

@Suppress("unused")
val removeSyncUltraMenuItemPatch = bytecodePatch(
    name = "Remove Sync Ultra menu item",
    description = "Removes the 'Sync Ultra' menu item from the app.",
) {
    compatibleWith(*SyncForRedditCompatible)

    execute {
        syncUltraMenuItemFingerprints.forEach { fingerprint ->
            fingerprint.matchAll().forEach { match ->
                val method = match.method
                val instructions = method.implementation?.instructions ?: return@forEach
                val indicesToRemove = mutableSetOf<Int>()

                // Remove a conservative window around each matched Sync Ultra key setup.
                match.stringMatches
                    .filter {
                        val value = it.string
                        value.contains("syncUltra", ignoreCase = true) ||
                            value.contains("ultraPremium", ignoreCase = true) ||
                            value.contains("mSyncUltraRow", ignoreCase = true)
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
}
