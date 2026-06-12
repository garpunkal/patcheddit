package app.morphe.patches.reddit.customclients.sync.syncforreddit.annoyances.random

import app.morphe.patcher.Fingerprint

// Fingerprint for the method that sets up the subreddit toolbar actions/menu
// This method typically inflates the menu and adds action items for random button
internal val subredditHeaderMenuInflateFingerprint = Fingerprint(
    strings = listOf("actionsRandom"),
    custom = { _, classDef ->
        // Avoid matching generic generated classes where these strings may also appear.
        classDef.sourceFile?.contains("Subreddit", ignoreCase = true) == true ||
            classDef.sourceFile?.contains("Toolbar", ignoreCase = true) == true ||
            classDef.sourceFile?.contains("Menu", ignoreCase = true) == true ||
            classDef.sourceFile?.contains("Main", ignoreCase = true) == true
    }
)

// Fingerprint for the method that sets up the random NSFW button
internal val subredditHeaderMenuInflateNsfwFingerprint = Fingerprint(
    strings = listOf("actionsRandomNsfw"),
    custom = { _, classDef ->
        classDef.sourceFile?.contains("Subreddit", ignoreCase = true) == true ||
            classDef.sourceFile?.contains("Toolbar", ignoreCase = true) == true ||
            classDef.sourceFile?.contains("Menu", ignoreCase = true) == true ||
            classDef.sourceFile?.contains("Main", ignoreCase = true) == true
    }
)
