package app.morphe.patches.reddit.customclients

import app.morphe.patcher.patch.BytecodePatch
import app.morphe.patches.all.misc.extension.activityOnCreateExtensionHook
import app.morphe.patches.all.misc.extension.sharedExtensionPatch

object ExtensionPatches {
    private fun hook(extensionName: String, vararg hookedClasses: String): BytecodePatch =
        sharedExtensionPatch(
            extensionName, *hookedClasses.map { activityOnCreateExtensionHook(it) }.toTypedArray()
        )
    internal val Sync = hook("syncforreddit", "Lcom/laurencedawson/reddit_sync/RedditApplication;")
}
