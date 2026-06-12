package app.morphe.patches.all.misc.interaction.gestures

import app.morphe.patcher.patch.resourcePatch
import org.w3c.dom.Element

@Suppress("unused")
internal val predictiveBackGesturePatch = resourcePatch(
    name = "Enable predictive back gesture",
    description = "Enables Android predictive back gesture support via AndroidManifest metadata.",
    default = false,
) {
    execute {
        document("AndroidManifest.xml").use { document ->
            val applicationNode =
                document
                    .getElementsByTagName("application")
                    .item(0) as Element

            applicationNode.setAttribute("android:enableOnBackInvokedCallback", "true")
        }
    }
}
