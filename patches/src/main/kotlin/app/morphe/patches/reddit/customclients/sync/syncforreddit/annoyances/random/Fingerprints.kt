package app.morphe.patches.reddit.customclients.sync.syncforreddit.annoyances.random

import app.morphe.patcher.Fingerprint

// Fingerprint for the method that sets up the subreddit toolbar actions/menu.
// Keep this broad so it still matches on obfuscated/repacked builds.
internal val subredditHeaderMenuInflateFingerprints = listOf(
    Fingerprint(strings = listOf("actionsRandom")),
    Fingerprint(strings = listOf("Random")),
)

// Fingerprint for the method that sets up the random NSFW button
internal val subredditHeaderMenuInflateNsfwFingerprints = listOf(
    Fingerprint(strings = listOf("actionsRandomNsfw")),
    Fingerprint(strings = listOf("Random NSFW")),
)
