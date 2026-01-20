package app.morphe.patches.reddit.customclients.sync.syncforreddit.annoyances.random

import app.morphe.patcher.Fingerprint

// Fingerprint for the method that sets up the subreddit toolbar actions/menu
// This method typically inflates the menu and adds action items for random button
internal val subredditHeaderMenuInflateFingerprint = Fingerprint(
    strings = listOf("actionsRandom"),
    custom = { method, _ ->
        // Target methods that set up the subreddit header menu/toolbar
        // Could be onCreate, onCreateMenu, onOptionsItemSelected, etc.
        true // Accept any method containing the "actionsRandom" string
    }
)

// Fingerprint for the method that sets up the random NSFW button
internal val subredditHeaderMenuInflateNsfwFingerprint = Fingerprint(
    strings = listOf("actionsRandomNsfw"),
    custom = { method, _ ->
        // Target methods that set up the subreddit header menu/toolbar
        true // Accept any method containing the "actionsRandomNsfw" string
    }
)
