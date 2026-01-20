package app.morphe.patches.reddit.customclients.sync.syncforreddit.annoyances.ultra

import app.morphe.patcher.extensions.InstructionExtensions.removeInstructions
import app.morphe.patcher.patch.bytecodePatch

@Suppress("unused")
val removeSyncUltraMenuItemPatch = bytecodePatch(
    name = "Remove Sync Ultra menu item",
    description = "Removes the 'Sync Ultra' menu item from the app.",
) {
    compatibleWith(
        "com.laurencedawson.reddit_sync"("v23.06.30-13:39"),
        "com.laurencedawson.reddit_sync.pro"(),
        "com.laurencedawson.reddit_sync.dev"(),
    )

    execute {
        // Patch the method that sets up the Sync Ultra menu item
        syncUltraMenuItemFingerprint.method.apply {
            val instructions = implementation!!.instructions
            
            // Find all instructions related to Sync Ultra menu setup and remove them
            val indicesToRemove = mutableListOf<Int>()
            
            instructions.forEachIndexed { index, instruction ->
                val instructionStr = instruction.toString()
                
                // Look for const string instructions with "Sync Ultra" or related strings
                if (instructionStr.contains("SyncUltra", ignoreCase = true) ||
                    instructionStr.contains("ultraPremium", ignoreCase = true) ||
                    instructionStr.contains("syncUltra", ignoreCase = true)) {
                    // Mark this instruction and potentially surrounding ones for removal
                    indicesToRemove.add(index)
                    
                    // Also check for menu add/setup patterns around this
                    for (offset in -2..2) {
                        val checkIdx = index + offset
                        if (checkIdx in 0 until instructions.size) {
                            val checkInstr = instructions[checkIdx].toString()
                            if (checkInstr.contains("setOnMenuItemClickListener") ||
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
