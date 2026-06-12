package app.morphe.patches.reddit.customclients.sync.syncforreddit.ultra

import app.morphe.patcher.extensions.InstructionExtensions.removeInstruction
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
            val matches = try {
                fingerprint.matchAll()
            } catch (_: Exception) {
                emptyList()
            }

            matches.forEach { match ->
                val instructions = match.method.implementation?.instructions ?: return@forEach
                val indicesToRemove = mutableSetOf<Int>()

                match.stringMatches
                    .filter {
                        val value = it.string
                        value.contains("syncUltra", ignoreCase = true) ||
                            value.contains("ultraPremium", ignoreCase = true) ||
                            value.contains("mSyncUltraRow", ignoreCase = true)
                    }
                    .forEach { stringMatch ->
                        for (offset in -10..20) {
                            val idx = stringMatch.index + offset
                            if (idx in 0 until instructions.size) {
                                indicesToRemove.add(idx)
                            }
                        }
                    }

                indicesToRemove
                    .sortedDescending()
                    .forEach { index ->
                        if (index < instructions.size) {
                            match.method.removeInstruction(index)
                        }
                    }
            }
        }
    }
}
