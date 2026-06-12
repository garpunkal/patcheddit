package app.morphe.patches.reddit.customclients.sync.syncforreddit.ultra

import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.reddit.customclients.sync.SyncForRedditCompatible
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction

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
                match.stringMatches
                    .filter {
                        val value = it.string
                        value.contains("syncUltra", ignoreCase = true) ||
                            value.contains("ultraPremium", ignoreCase = true) ||
                            value.contains("mSyncUltraRow", ignoreCase = true)
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
