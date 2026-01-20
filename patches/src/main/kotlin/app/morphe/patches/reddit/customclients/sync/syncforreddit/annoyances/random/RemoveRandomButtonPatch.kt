package app.morphe.patches.reddit.customclients.sync.syncforreddit.annoyances.random

import app.morphe.patcher.extensions.InstructionExtensions.removeInstructions
import app.morphe.patcher.patch.bytecodePatch

@Suppress("unused")
val removeRandomButtonPatch = bytecodePatch(
    name = "Remove random button",
    description = "Removes the random subreddit navigation button from the subreddit header.",
) {
    compatibleWith(
        "com.laurencedawson.reddit_sync"("v23.06.30-13:39"),
        "com.laurencedawson.reddit_sync.pro"(),
        "com.laurencedawson.reddit_sync.dev"(),
    )

    execute {
        // Patch the method that sets up the random subreddit action
        subredditHeaderMenuInflateFingerprint.method.apply {
            val instructions = implementation!!.instructions
            
            // Find all instructions related to the random action and remove them
            val indicesToRemove = mutableListOf<Int>()
            
            instructions.forEachIndexed { index, instruction ->
                val instructionStr = instruction.toString()
                
                // Look for const string instructions with "actionsRandom"
                if (instructionStr.contains("actionsRandom")) {
                    // Mark this instruction and potentially surrounding ones for removal
                    indicesToRemove.add(index)
                    
                    // Also check for menu add/setup patterns around this
                    // Typically: const-string -> setId or similar pattern
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
