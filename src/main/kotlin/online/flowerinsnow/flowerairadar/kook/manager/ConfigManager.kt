package online.flowerinsnow.flowerairadar.kook.manager

import love.forte.simbot.LoggerFactory
import online.flowerinsnow.flowerairadar.kook.util.IOUtils
import online.flowerinsnow.xmlreader.api.node.XMLNode
import online.flowerinsnow.xmlreader.core.XMLReader
import java.io.File
import java.nio.file.Files
import kotlin.io.path.Path

object ConfigManager {
    private val logger = LoggerFactory.getLogger<ConfigManager>()

    var configFile = "farkook.xml"

    var clientID = ""
    var token = CharArray(0)

    var sqlHost = ""
    var sqlPort = ""
    var sqlUser = ""
    var sqlPassword = CharArray(0)
    var sqlSchema = ""
    var sqlMaxActive = ""
    var sqlMaxWait = ""
    var sqlInitialSize = ""

    fun load() : Boolean {
        val file = File(configFile)
        if (!file.exists()) {
            logger.warn("配置文件不存在，将创建一份默认 $configFile")
            val default = ConfigManager::class.java.getResourceAsStream("/farkook.xml") ?: return false
            Files.copy(default, Path(configFile))
            IOUtils.closeQuietly(default)
            return false
        }

        val root = XMLReader.readAsNode(file) // 读取XML配置文件
        val childs = root.childNodes // 获取所有子节点
        val configuration = childs["configuration"] ?: return false // 获取配置节点
        if (!readRoot(configuration)) { // 读取配置节点 如果未准备则关闭程序
            logger.error("配置文件未能准备就绪，请在\"configuration\"将\"enabled\"节点设为true")
            return false
        }
        return true
    }

    private fun readRoot(node : XMLNode) : Boolean {
        val childs = node.childNodes // 获取所有子节点
        val enabled = childs["enabled"]?.valueAsBool ?: false // 子节点下 enabled是否为真

        if (!enabled) { // 不为真 关闭程序
            return false
        }

        // 阅读子节点
        val account = childs["account"]
        if (account != null) {
            readAccount(account)
        }

        val database = childs["database"]
        if (database != null) {
            readDatabase(database)
        }
        return true
    }

    private fun readAccount(node : XMLNode) {
        val childs = node.childNodes
        clientID = childs["clientID"]?.value ?: ""
        token = childs["token"]?.value?.toCharArray() ?: CharArray(0)
    }

    private fun readDatabase(node : XMLNode) {
        val childs = node.childNodes
        sqlHost = childs["host"]?.value ?: ""
        sqlUser = childs["user"]?.value ?: ""
        sqlPort = childs["port"]?.value ?: ""
        sqlPassword = childs["password"]?.value?.toCharArray() ?: CharArray(0)
        sqlSchema = childs["schema"]?.value ?: ""
        sqlMaxActive = childs["maxActive"]?.value ?: ""
        sqlMaxWait = childs["maxWait"]?.value ?: ""
        sqlInitialSize = childs["initialSize"]?.value ?: ""
    }
}