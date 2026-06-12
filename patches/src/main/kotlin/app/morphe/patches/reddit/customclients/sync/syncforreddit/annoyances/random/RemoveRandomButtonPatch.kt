package app.morphe.patches.reddit.customclients.sync.syncforreddit.annoyances.random

import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.reddit.customclients.sync.SyncForRedditCompatible
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction

@Suppress("unused")
val removeRandomButtonPatch = bytecodePatch(
    name = "Remove random button",
    description = "Removes the random subreddit navigation button from the subreddit header.",
) {
    compatibleWith(*SyncForRedditCompatible)

    execute {
        subredditHeaderMenuInflateFingerprints.forEach { fingerprint ->
            val matches = try {
                fingerprint.matchAll()
            } catch (_: Exception) {
                emptyList()
            }

            matches.forEach { match ->
                match.stringMatches
                    .filter {
                        val value = it.string
                        value.contains("actionsRandom", ignoreCase = true) ||
                            value.equals("Random", ignoreCase = true)
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
