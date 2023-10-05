package online.jutter.ui.ext

import java.util.logging.Level
import java.util.logging.Logger

fun logDebug(tag: String, message: String) {
    Logger.getLogger("fractals").log(Level.INFO, "LOG_TAG:$tag $message")
}