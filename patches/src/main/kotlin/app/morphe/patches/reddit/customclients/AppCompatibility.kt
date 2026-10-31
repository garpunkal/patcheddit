package app.morphe.patches.reddit.customclients

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

object AppCompatibility {
    internal val SyncForRedditFree = Compatibility(
        name = "Sync for Reddit",
        packageName = "com.laurencedawson.reddit_sync",
        targets = listOf(AppTarget(version = "v23.06.30-13:39"))
    )

    internal val SyncForReddit = arrayOf(
        SyncForRedditFree,
        Compatibility(
            name = "Sync for Reddit Pro",
            packageName = "com.laurencedawson.reddit_sync.pro",
            targets = listOf(AppTarget(version = null))
        ),
        Compatibility(
            name = "Sync for Reddit Dev",
            packageName = "com.laurencedawson.reddit_sync.dev",
            targets = listOf(AppTarget(version = null))
        )
    )

}
