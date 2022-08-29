package online.flowerinsnow.flowerairadar.kook.manager

import love.forte.simbot.LoggerFactory
import online.flowerinsnow.flowerairadar.kook.annotations.UsingCache
import online.flowerinsnow.flowerairadar.kook.database.DatabaseTables
import online.flowerinsnow.flowerairadar.kook.`object`.DefaultSQLExceptionHandler
import online.flowerinsnow.flowerairadar.kook.`object`.Permission
import online.flowerinsnow.flowerairadar.kook.util.IOUtils
import online.flowerinsnow.flowerairadar.kook.util.LoggingUtils
import java.sql.SQLException

/*
 * 权限管理机制
 * 每个用户都有自己的权限列表，可以在不同的服务器拥有不同的权限
 *
 * 权限 (permission) 一些特殊的：
 * root 代表所有权限
 * base 代表基础权限 如果用户没有任何一个权限则自动获取
 *
 * 作用域 (scope) 为服务器ID 代表用户在此服务器中拥有单独的权限 一些特殊的：
 * $ 代表私聊
 * @ 代表所有服务器
 * # 代表所有位置
 */
@UsingCache
object PermissionManager {
    private val logger = LoggerFactory.getLogger<PermissionManager>()
    @UsingCache
    private val cached : MutableMap<String, MutableList<Permission>> = HashMap()
    fun setPermission(id : String, permission : String, scope : String) {
        DatabaseManager.getSQLManager().createReplace(DatabaseTables.PERMISSION.table.tableName)
            .setColumnNames("user", "permission", "scope")
            .setParams(id, permission, scope)
            .execute(DefaultSQLExceptionHandler(logger))
    }

    fun hasPermission(id : String, permission : String, scope : String) : Boolean {
        val cachedUser = cached[id]
        if (cachedUser != null) {
            return cachedUser.any {
                it.permission == permission && it.scope == scope
            }
        }

        val action = DatabaseManager.getSQLManager().createQuery().inTable(DatabaseTables.PERMISSION.table.tableName)
        action.addCondition("user", id)
        action.addCondition("permission", permission)
        when (scope) {
            "@" -> {
                action.addCondition("`scope` = '@' OR `scope` = '#'")
            }
            "#" -> {
                action.addCondition("scope", "#")
            }
            "$" -> {
                action.addCondition("`scope` = '$' OR `scope` = '#'")
            }
            else -> {
                action.addCondition("scope", scope)
            }
        }
        val query = action.build().execute(DefaultSQLExceptionHandler(logger))
        if (query != null) {
            try {
                val res = query.resultSet
                return res.next()
            } catch (ex : SQLException) {
                LoggingUtils.throwing(logger, ex)
            } finally {
                IOUtils.closeQuietly(query)
            }
        }
        return false
    }

    /**
     * 删除权限节点，三个参数如果某一个不填则不限
     *
     * @param id 用户ID
     * @param permission 权限节点
     * @param scope 作用域
     */
    fun unsetPermission(id : String? = null, permission : String? = null, scope: String? = null) {
        val action = DatabaseManager.getSQLManager().createDelete(DatabaseTables.PERMISSION.table.tableName)
        if (id != null) {
            action.addCondition("user", id)
        }
        if (permission != null) {
            action.addCondition("permission", permission)
        }
        if (scope != null) {
            action.addCondition("scope", scope)
        }

        action.build().execute(DefaultSQLExceptionHandler(logger))
    }

    fun cacheUser(id : String) {
        var cachedUser = cached[id]
        if (cachedUser != null) {
            clearCacheUser(id)
        }
        cachedUser = ArrayList()
        cached[id] = cachedUser

        val action = DatabaseManager.getSQLManager().createQuery().inTable(DatabaseTables.PERMISSION.table.tableName)
        action.addCondition("user", id)
        val query = action.build().execute(DefaultSQLExceptionHandler(logger)) ?: return
        try {
            val res = query.resultSet
            while (res.next()) {
                cachedUser.add(Permission(res.getString("permission"), res.getString("scope")))
            }
        } catch (ex : SQLException) {
            LoggingUtils.throwing(logger, ex)
        } finally {
            IOUtils.closeQuietly(query)
        }
    }

    fun clearCacheUser(id : String? = null) {
        if (id == null) {
            cached.clear()
        } else {
            cached.remove(id)
        }
    }
}