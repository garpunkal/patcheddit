package app.morphe.patches.reddit.customclients.sync.syncforreddit.annoyances.random

import app.morphe.patcher.extensions.InstructionExtensions.removeInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.reddit.customclients.sync.SyncForRedditCompatible

@Suppress("unused")
val removeRandomNsfwButtonPatch = bytecodePatch(
    name = "Remove random NSFW button",
    description = "Removes the random NSFW subreddit navigation button from the subreddit header.",
) {
    compatibleWith(*SyncForRedditCompatible)

    execute {
        subredditHeaderMenuInflateNsfwFingerprints.forEach { fingerprint ->
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
                        value.contains("random", ignoreCase = true) &&
                            value.contains("nsfw", ignoreCase = true)
                    }
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
                            match.method.removeInstruction(index)
                        }
                    }
            }
        }
    }
}