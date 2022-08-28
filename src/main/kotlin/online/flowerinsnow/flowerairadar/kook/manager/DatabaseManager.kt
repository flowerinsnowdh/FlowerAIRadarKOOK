package online.flowerinsnow.flowerairadar.kook.manager

import cc.carm.lib.easysql.api.SQLManager
import cc.carm.lib.easysql.manager.SQLManagerImpl
import com.alibaba.druid.pool.DruidDataSource
import com.alibaba.druid.pool.DruidDataSourceFactory
import online.flowerinsnow.flowerairadar.kook.util.IOUtils

object DatabaseManager {
    private var ds : DruidDataSource? = null
    private var sql : SQLManagerImpl? = null

    fun getSQLManager() : SQLManager {
        if (isConnected()) {
            return sql!!
        } else {
            throw IllegalStateException("Database not connect yet.")
        }
    }

    fun connect() {
        val config = HashMap<String, String>()
        config[DruidDataSourceFactory.PROP_DRIVERCLASSNAME] = "com.mysql.cj.jdbc.Driver"
        config[DruidDataSourceFactory.PROP_URL] = "jdbc:mysql://${ConfigManager.sqlHost}:${ConfigManager.sqlPort}/${ConfigManager.sqlSchema}"
        config[DruidDataSourceFactory.PROP_USERNAME] = ConfigManager.sqlUser
        config[DruidDataSourceFactory.PROP_PASSWORD] = String(ConfigManager.sqlPassword)
        config[DruidDataSourceFactory.PROP_MAXACTIVE] = ConfigManager.sqlMaxActive
        config[DruidDataSourceFactory.PROP_MAXWAIT] = ConfigManager.sqlMaxWait
        config[DruidDataSourceFactory.PROP_INITIALSIZE] = ConfigManager.sqlInitialSize
        ds = DruidDataSourceFactory.createDataSource(config) as DruidDataSource?
        ds!!.connection.close()
        sql = SQLManagerImpl(ds!!)
    }

    fun disconnect() {
        if (isConnected()) {
            IOUtils.closeQuietly(ds)
            ds = null
            sql = null
        }
    }

    fun isConnected() : Boolean {
        return ds != null
    }

    fun reconnect() {
        disconnect()
        connect()
    }
}