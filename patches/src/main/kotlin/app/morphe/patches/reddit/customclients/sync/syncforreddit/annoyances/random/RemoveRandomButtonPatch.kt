package app.morphe.patches.reddit.customclients.sync.syncforreddit.annoyances.random

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.reddit.customclients.sync.SyncForRedditCompatible

@Suppress("unused")
val removeRandomButtonPatch = bytecodePatch(
    name = "Remove random button",
    description = "Removes the random subreddit navigation button from the subreddit header.",
) {
    compatibleWith(*SyncForRedditCompatible)

    execute {
        subredditHeaderMenuInflateFingerprint.matchAll().forEach { match ->
            val method = match.method
            val instructions = method.implementation?.instructions ?: return@forEach
            val indicesToRemove = mutableSetOf<Int>()

            // Remove a conservative window around each matched random action key setup.
            match.stringMatches
                .filter { it.string.contains("actionsRandom", ignoreCase = true) }
                .forEach { stringMatch ->
                    val baseIndex = stringMatch.index
                    for (offset in -10..16) {
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
