package online.flowerinsnow.flowerairadar.kook.manager

import com.alibaba.druid.pool.DruidDataSource
import com.alibaba.druid.pool.DruidDataSourceFactory
import online.flowerinsnow.flowerairadar.kook.util.IOUtils

object DatabaseManager {
    private var conn : DruidDataSource? = null

    fun getConnection() : DruidDataSource {
        if (isConnected()) {
            return conn!!
        } else {
            throw IllegalStateException("Database not connect yet.")
        }
    }

    fun connect() {
        val config = HashMap<String, String>()
        config[DruidDataSourceFactory.PROP_URL] = "jdbc:mysql://${ConfigManager.sqlHost}:${ConfigManager.sqlPort}/${ConfigManager.sqlSchema}"
        config[DruidDataSourceFactory.PROP_USERNAME] = ConfigManager.sqlUser
        config[DruidDataSourceFactory.PROP_PASSWORD] = String(ConfigManager.sqlPassword)
        config[DruidDataSourceFactory.PROP_MAXACTIVE] = ConfigManager.sqlMaxActive
        config[DruidDataSourceFactory.PROP_MAXWAIT] = ConfigManager.sqlMaxWait
        config[DruidDataSourceFactory.PROP_INITIALSIZE] = ConfigManager.sqlInitialSize
        conn = DruidDataSourceFactory.createDataSource(config) as DruidDataSource?
    }

    fun disconnect() {
        if (isConnected()) {
            IOUtils.closeQuietly(conn)
            conn = null
        }
    }

    fun isConnected() : Boolean {
        return conn != null
    }

    fun reconnect() {
        disconnect()
        connect()
    }
}