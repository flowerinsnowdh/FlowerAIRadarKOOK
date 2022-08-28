package online.flowerinsnow.flowerairadar.kook

import love.forte.simboot.core.application.BootApplication
import love.forte.simboot.core.application.createBootApplication
import love.forte.simbot.component.kook.kookBots
import love.forte.simbot.component.kook.useKook
import online.flowerinsnow.flowerairadar.kook.manager.ConfigManager
import online.flowerinsnow.flowerairadar.kook.manager.DatabaseManager

//@SimbootApplication
//class Main {
//    companion object {
//        lateinit var application : BootApplication
//    }
//}
//
//suspend fun main(vararg args : String) {
//    // 读取配置文件
//    ConfigManager.configFile = System.getProperty("farkook.config", ConfigManager.configFile)
//    println("读取配置文件 ${ConfigManager.configFile}... 需要指定其它配置文件请修改JVM参数\"farkook.config\"")
//    if (!ConfigManager.load()) {
//        return
//    }
//
//    // 连接数据库
//    println("连接数据库...")
//    DatabaseManager.connect()
//
//    // 启动机器人
//    println("启动机器人...")
//    Main.application = SimbootApp.run(Main::class, args = args)
//    Main.application.join()
//}

class Main {
    companion object {
        lateinit var application : BootApplication
    }
}

suspend fun main(vararg args : String) {
    // 读取配置文件
    ConfigManager.configFile = System.getProperty("farkook.config", ConfigManager.configFile)
    println("读取配置文件 ${ConfigManager.configFile}... 需要指定其它配置文件请修改JVM参数\"farkook.config\"")
    if (!ConfigManager.load()) {
        return
    }

    // 连接数据库
    println("连接数据库...")
    DatabaseManager.connect()

    // 启动机器人
    println("启动机器人...")
    Main.application = createBootApplication() {
        useKook()

        kookBots {
            register(clientId = ConfigManager.clientID, token = String(ConfigManager.token)).start()
        }
    }
    Main.application.join()
}