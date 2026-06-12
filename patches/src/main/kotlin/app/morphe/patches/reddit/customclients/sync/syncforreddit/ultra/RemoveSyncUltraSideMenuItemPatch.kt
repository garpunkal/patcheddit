package app.morphe.patches.reddit.customclients.sync.syncforreddit.ultra

import app.morphe.patcher.extensions.InstructionExtensions.removeInstruction
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
                        value.contains("mUpgradeRow", ignoreCase = true) ||
                            value.contains("mSyncUltraRow", ignoreCase = true) ||
                            value.contains("onUpgradeClicked", ignoreCase = true) ||
                            value.contains("getSync", ignoreCase = true)
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