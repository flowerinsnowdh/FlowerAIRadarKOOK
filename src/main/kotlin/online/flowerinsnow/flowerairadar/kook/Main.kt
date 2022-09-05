package online.flowerinsnow.flowerairadar.kook

import love.forte.simboot.core.application.BootApplication
import love.forte.simboot.core.application.createBootApplication
import love.forte.simbot.LoggerFactory
import love.forte.simbot.component.kook.kookBots
import love.forte.simbot.component.kook.useKook
import online.flowerinsnow.flowerairadar.kook.database.DatabaseTables
import online.flowerinsnow.flowerairadar.kook.manager.ConfigManager
import online.flowerinsnow.flowerairadar.kook.manager.DatabaseManager
import online.flowerinsnow.flowerairadar.kook.manager.PermissionManager

class Main {
    companion object {
        lateinit var application : BootApplication

        suspend fun start() {
            a()
        }
    }
}

suspend fun a() {
    val logger = LoggerFactory.getLogger<Main>()
    // 读取配置文件
    ConfigManager.configFile = System.getProperty("farkook.config", ConfigManager.configFile)
    logger.info("读取配置文件 ${ConfigManager.configFile}... 需要指定其它配置文件请修改JVM参数\"farkook.config\"")
    if (!ConfigManager.load()) {
        return
    }

    // 连接数据库
    logger.info("连接数据库...")
    DatabaseManager.connect()

    logger.info("创建数据库表...")
    DatabaseTables.createAll(DatabaseManager.getSQLManager())

    // 启动机器人
    logger.info("启动机器人...")
    Main.application = createBootApplication(configurator = {
        classesScanPackage = listOf("online.flowerinsnow.flowerairadar.kook")
    }) {
        useKook()

        kookBots {
            register(clientId = ConfigManager.clientID, token = String(ConfigManager.token)).start()
        }
    }
    Main.application.join()
}

suspend fun restart() {
    val logger = LoggerFactory.getLogger<Main>()
    logger.info("停止机器人...")
    Main.application.shutdown()
    
    logger.info("断开数据库...")
    DatabaseManager.disconnect()

    logger.info("清除缓存...")
    PermissionManager.clearCacheUser()

    logger.info("正在重启...")
    main()
}