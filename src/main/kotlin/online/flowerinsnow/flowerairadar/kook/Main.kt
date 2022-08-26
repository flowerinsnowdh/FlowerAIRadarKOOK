package online.flowerinsnow.flowerairadar.kook

import love.forte.simbot.component.kook.KookComponent
import love.forte.simbot.component.kook.useKook
import love.forte.simbot.core.application.SimpleApplication
import love.forte.simbot.core.application.createSimpleApplication
import online.flowerinsnow.flowerairadar.kook.manager.ConfigManager
import online.flowerinsnow.flowerairadar.kook.manager.DatabaseManager
import org.apache.logging.log4j.LogManager

lateinit var application : SimpleApplication
val logger = LogManager.getLogger()

suspend fun main() {
    // 读取配置文件
    ConfigManager.configFile = System.getProperty("farkook.config", ConfigManager.configFile)
    logger.info("读取配置文件 ${ConfigManager.configFile}... 需要指定其它配置文件请修改JVM参数\"farkook.config\"")
    if (!ConfigManager.load()) {
        return
    }

    logger.info("连接数据库...")
    DatabaseManager.connect()

    // 启动机器人
    logger.info("启动机器人...")
    application = createSimpleApplication {
        useKook {
            component {
                install(KookComponent)
            }
            botManager {
                @Suppress("DEPRECATION")
                register(clientId = ConfigManager.clientID, token = String(ConfigManager.token)) { bot ->
                    it.onCompletion {
                        bot.start()
                    }
                }
            }
        }
    }
}