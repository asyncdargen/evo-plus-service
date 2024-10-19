package ru.dargen.evoplus.service

import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.RemovalCause
import com.github.benmanes.caffeine.cache.Scheduler
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import ru.dargen.evoplus.data.Player
import java.time.Duration.between
import java.time.Instant.now
import java.util.concurrent.TimeUnit

@Service
class PlayerService(@Lazy private val discordService: DiscordService) {

    private val logger = java.util.logging.Logger.getLogger("PlayerService")

    private val cache = Caffeine.newBuilder()
        .scheduler(Scheduler.systemScheduler())
        .expireAfterWrite(1, TimeUnit.MINUTES)
        .removalListener(::expired)
        .build<String, Player>()

    val ingameList get() = cache.asMap().values
    val count get() = cache.asMap().size

    fun updateIngame(username: String, version: String) {
        val name = username.lowercase()
        val player = cache.getIfPresent(name)?.also { it.update(version) }
            ?: Player(username, version = version).apply { log("Player $username joined with EvoPlus v$version") }

        cache.put(name, player)
    }

    fun isIngame(username: String) = cache.getIfPresent(username.lowercase()) != null

    fun filterIngame(usernames: Collection<String>) = usernames.filter(this::isIngame)

    private fun expired(name: String?, player: Player?, cause: RemovalCause) {
        player ?: return
        if (cause === RemovalCause.EXPIRED) {
            log(
                "Player ${player.name} leave the game (playtime ${
                    between(player.joinTimestamp, now()).toMinutes()
                } minutes)"
            )
        }
    }

    private fun log(text: String) {
        logger.info(text)
        discordService.log(text)
    }

}