package online.flowerinsnow.flowerairadar.kook.util

import org.slf4j.Logger

object LoggingUtils {
    fun throwing(logger : Logger, throwable : Throwable, caused : Boolean = false) {
        if (caused) {
            logger.error("Caused by $throwable")
        } else {
            logger.error(throwable.toString())
        }
        throwable.stackTrace.forEach {
            logger.error("\tat $it")
        }
        val cause = throwable.cause
        if (cause != null && cause != throwable) {
            throwing(logger, throwable, true)
        }
    }
}