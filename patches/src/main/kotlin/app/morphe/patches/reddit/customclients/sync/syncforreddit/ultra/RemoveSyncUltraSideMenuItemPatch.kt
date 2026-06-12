package app.morphe.patches.reddit.customclients.sync.syncforreddit.ultra

import app.morphe.patcher.extensions.InstructionExtensions.removeInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.reddit.customclients.sync.SyncForRedditCompatible

@Suppress("unused")
val removeSyncUltraSideMenuItemPatch = bytecodePatch(
    name = "Remove 'Get Sync Ultra' sidemenu item",
    description = "Removes the 'Get Sync Ultra' item from the navigation sidemenu.",
) {
    compatibleWith(*SyncForRedditCompatible)

    execute {
        // Patch the method that sets up the Sync Ultra sidemenu item
        getSyncUltraMenuItemFingerprint.method.apply {
            val instructions = implementation!!.instructions
            
            // Find all instructions related to "Get Sync Ultra" sidemenu setup and remove them
            val indicesToRemove = mutableListOf<Int>()
            
            instructions.forEachIndexed { index, instruction ->
                val instructionStr = instruction.toString()
                
                // Look for string references to "Get Sync Ultra" or upgrade/premium related strings
                if (instructionStr.contains("mUpgradeRow", ignoreCase = true) ||
                    instructionStr.contains("getSync", ignoreCase = true) ||
                    instructionStr.contains("ultraRow", ignoreCase = true) ||
                    instructionStr.contains("onUpgradeClicked", ignoreCase = true)) {
                    // Mark this instruction and potentially surrounding ones for removal
                    indicesToRemove.add(index)
                    
                    // Also check for sidemenu add/setup patterns around this
                    for (offset in -2..2) {
                        val checkIdx = index + offset
                        if (checkIdx in 0 until instructions.size) {
                            val checkInstr = instructions[checkIdx].toString()
                            if (checkInstr.contains("setOnClickListener") ||
                                checkInstr.contains("add(") ||
                                checkInstr.contains("invoke")) {
                                if (checkIdx !in indicesToRemove) {
                                    indicesToRemove.add(checkIdx)
                                }
                            }
                        }
                    }
                }
            }
            
            // Remove in reverse order to maintain valid indices
            indicesToRemove.distinct().sortedByDescending { it }.forEach { index ->
                if (index < instructions.size) {
                    try {
                        instructions.removeAt(index)
                    } catch (e: Exception) {
                        // Silently skip if removal fails
                    }
                }
            }
        }
    }
}
