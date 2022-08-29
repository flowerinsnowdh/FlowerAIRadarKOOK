package online.flowerinsnow.flowerairadar.kook.listener

import love.forte.di.annotation.Beans
import love.forte.simboot.annotation.Listener
import love.forte.simbot.LoggerFactory
import love.forte.simbot.event.ChannelMessageEvent

@Beans
class TestListener {
    private val logger = LoggerFactory.getLogger<TestListener>()
    @Listener
    suspend fun ChannelMessageEvent.testListener() {
        if (messageContent.plainText == "/myid") {
            reply("您的ID是 ${author().id}")
        }
    }
}