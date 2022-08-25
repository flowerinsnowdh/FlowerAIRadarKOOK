package online.flowerinsnow.flowerairadar.kook.config

import online.flowerinsnow.flowerairadar.kook.util.IOUtils
import online.flowerinsnow.flowerairadar.kook.util.XMLUtils
import org.apache.logging.log4j.LogManager
import org.w3c.dom.Document
import org.w3c.dom.Node
import java.io.File
import java.nio.file.Files
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.io.path.Path

object ConfigManager {
    val logger = LogManager.getLogger()
    var configFile = "farkook.xml"

    var clientID = ""
    var token = CharArray(0)

    var sqlHost = ""
    var sqlUser = ""
    var sqlPassword = CharArray(0)

    fun load() : Boolean {
        val file = File(configFile)
        if (!file.exists()) {
            logger.warn("配置文件不存在，将创建一份默认 $configFile")
            val default = ConfigManager::class.java.getResourceAsStream("/farkook.xml") ?: return false
            Files.copy(default, Path(configFile))
            IOUtils.closeQuietly(default)
            return false
        }
        val factory = DocumentBuilderFactory.newInstance()
        val builder = factory.newDocumentBuilder()
        val document = builder.parse(configFile)
        if (!readRoot(document)) {
            logger.error("配置文件未能准备就绪，请在\"configuration\"将\"enabled\"节点设为true")
            return false
        }
        return true
    }

    private fun readRoot(document : Document) : Boolean {
        val list = document.getElementsByTagName("enabled")
        if (list.length == 0 || !"true".equals(list.item(0).nodeValue, true)) {
            return false
        }

        XMLUtils.childNodesForEach(document) {
            when (it.nodeName) {
                "account" -> {
                    readAccount(it)
                }
                "database" -> {
                    readDatabase(it)
                }
            }
        }
        return true
    }

    private fun readAccount(node : Node) {
        XMLUtils.childNodesForEach(node) {
            when (it.nodeName) {
                "clientID" -> {
                    clientID = it.nodeValue
                }
                "token" -> {
                    token = it.nodeValue.toCharArray()
                }
            }
        }
    }

    private fun readDatabase(node : Node) {
        XMLUtils.childNodesForEach(node) {
            when (it.nodeName) {
                "host" -> {
                    sqlHost = it.nodeValue
                }
                "user" -> {
                    sqlUser = it.nodeValue
                }
                "password" -> {
                    sqlPassword = it.nodeValue.toCharArray()
                }
            }
        }
    }
}