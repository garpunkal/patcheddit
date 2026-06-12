package app.morphe.patches.reddit.customclients.sync.syncforreddit.ultra

import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.reddit.customclients.sync.SyncForRedditCompatible
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction

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
                match.stringMatches
                    .filter {
                        val value = it.string
                        value.contains("mUpgradeRow", ignoreCase = true) ||
                            value.contains("mSyncUltraRow", ignoreCase = true) ||
                            value.contains("onUpgradeClicked", ignoreCase = true) ||
                            value.contains("getSync", ignoreCase = true)
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
