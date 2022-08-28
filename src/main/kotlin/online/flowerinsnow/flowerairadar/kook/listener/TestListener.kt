package online.flowerinsnow.flowerairadar.kook.listener

import love.forte.di.annotation.Beans
import love.forte.simboot.annotation.Listener
import love.forte.simbot.event.ChannelMessageEvent
import org.apache.logging.log4j.kotlin.Logging

@Beans
class TestListener : Logging {
    @Listener
    suspend fun ChannelMessageEvent.testListener() {
        logger.info("Author: ${author().username} Channel: ${channel().name} Guild: ${channel().guild().name} Context: ${messageContent.plainText}")
    }
}