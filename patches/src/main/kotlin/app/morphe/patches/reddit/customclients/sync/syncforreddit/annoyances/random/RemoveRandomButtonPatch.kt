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
        val matches = try {
            subredditHeaderMenuInflateFingerprint.matchAll()
        } catch (_: Exception) {
            emptyList()
        }

        matches.forEach { match ->
            val instructions = match.method.implementation?.instructions ?: return@forEach
            val indicesToRemove = mutableSetOf<Int>()

            match.stringMatches
                .filter { it.string.contains("actionsRandom", ignoreCase = true) }
                .forEach { stringMatch ->
                    for (offset in -10..16) {
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
                        try {
                            instructions.removeAt(index)
                        } catch (_: Exception) {
                            // Skip invalid removals and continue patching.
                        }
                    }
                }
        }
    }
}
