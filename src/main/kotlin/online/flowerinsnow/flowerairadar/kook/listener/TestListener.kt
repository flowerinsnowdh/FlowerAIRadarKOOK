package online.flowerinsnow.flowerairadar.kook.listener

import love.forte.di.annotation.Beans
import love.forte.simboot.annotation.Filter
import love.forte.simboot.annotation.Listener
import love.forte.simboot.filter.MatchType
import love.forte.simbot.LoggerFactory
import love.forte.simbot.event.ChannelMessageEvent

@Beans
class TestListener {
    private val logger = LoggerFactory.getLogger<TestListener>()
    @Listener
    @Filter("/myid", matchType = MatchType.TEXT_EQUALS)
    suspend fun ChannelMessageEvent.testListener() {
        reply("您的ID是 ${author().id}")
    }
}