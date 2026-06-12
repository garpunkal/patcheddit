package app.morphe.patches.reddit.customclients.sync.syncforreddit.annoyances.random

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.reddit.customclients.sync.SyncForRedditCompatible

@Suppress("unused")
val removeRandomNsfwButtonPatch = bytecodePatch(
    name = "Remove random NSFW button",
    description = "Removes the random NSFW subreddit navigation button from the subreddit header.",
) {
    compatibleWith(*SyncForRedditCompatible)

    execute {
        val method = subredditHeaderMenuInflateNsfwFingerprint.methodOrNull ?: return@execute
        val instructions = method.implementation?.instructions ?: return@execute
        val indicesToRemove = mutableSetOf<Int>()

        // Scan for known random NSFW markers and remove nearby setup instructions.
        instructions.forEachIndexed { index, instruction ->
            val value = instruction.toString()
            if (
                value.contains("actionsRandomNsfw", ignoreCase = true) ||
                value.contains("randomNsfw", ignoreCase = true)
            ) {
                for (offset in -10..16) {
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
                        // Skip invalid removals and continue patching.
                    }
                }
            }
    }
}
