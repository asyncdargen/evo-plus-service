package ru.dargen.evoplus.service.impl

import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.RemovalCause
import com.github.benmanes.caffeine.cache.Scheduler
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import ru.dargen.evoplus.data.Player
import ru.dargen.evoplus.service.DiscordService
import ru.dargen.evoplus.service.PlayerService
import java.util.concurrent.TimeUnit
import java.util.logging.Logger

@Service
class PlayerServiceImpl(
    @Lazy private val discordService: DiscordService,
) : PlayerService {

    private val logger = Logger.getLogger("PlayerService")

    private val cache = Caffeine.newBuilder()
        .scheduler(Scheduler.systemScheduler())
        .expireAfterWrite(1, TimeUnit.MINUTES)
        .removalListener(::expired)
        .build<String, Player>()

    override val players get() = cache.asMap().values
    override val playersCount get() = cache.asMap().size

    override fun updateIngame(username: String, version: String) {
        val name = username.lowercase()
        val player = cache.getIfPresent(name)?.also { it.update(version) }
            ?: Player(username, version).apply { log("Player $username joined with v$version") }

        cache.put(name, player)
    }

    override fun isIngame(username: String) = cache.getIfPresent(username.lowercase()) != null

    override fun filterIngame(usernames: Collection<String>) = usernames.filter(this::isIngame)

    private fun expired(name: String?, player: Player?, cause: RemovalCause) {
        player ?: return
        if (cause === RemovalCause.EXPIRED) {
            log("Player ${player.name} leave the game (playtime ${player.onlineTime} minutes)")
        }
    }

    private fun log(text: String) {
        logger.info(text)
        discordService.log("```$text```")
    }

}
