package app.morphe.patches.reddit.customclients.sync.syncforreddit.annoyances.random

import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.reddit.customclients.sync.SyncForRedditCompatible
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction

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
                match.stringMatches
                    .filter {
                        val value = it.string
                        value.contains("actionsRandomNsfw", ignoreCase = true) ||
                            value.contains("Random NSFW", ignoreCase = true)
                    }
                    .forEach { stringMatch ->
                        runCatching {
                            val register =
                                match.method
                                    .getInstruction<OneRegisterInstruction>(stringMatch.index)
                                    .registerA
                            val replacement = "__morphe_removed_${stringMatch.string}__"
                            match.method.replaceInstruction(
                                stringMatch.index,
                                "const-string v$register, \"$replacement\"",
                            )
                        }
                    }
            }
        }
    }
}
