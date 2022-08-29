package online.flowerinsnow.flowerairadar.kook.`object`

import cc.carm.lib.easysql.api.SQLAction
import cc.carm.lib.easysql.api.function.SQLExceptionHandler
import online.flowerinsnow.flowerairadar.kook.util.LoggingUtils
import org.slf4j.Logger
import java.sql.SQLException

class DefaultSQLExceptionHandler(private val logger : Logger) : SQLExceptionHandler {
    override fun accept(t : SQLException, u : SQLAction<*>) {
        logger.error(u.sqlContent)
        LoggingUtils.throwing(logger, t)
    }

    companion object {
        fun ofLogger(logger : Logger) : DefaultSQLExceptionHandler {
            return DefaultSQLExceptionHandler(logger)
        }
    }
}