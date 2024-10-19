package ru.dargen.evoplus.service

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Activity
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Lazy
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers
import java.util.concurrent.TimeUnit.SECONDS

@Service
class DiscordService(
    private val jda: JDA,

    @Qualifier("discord.webhook.log") private val logWebhookURL: String,
    @Lazy private val playerService: PlayerService,
) {

    private val httpClient = HttpClient.newHttpClient()

    fun log(text: String) {
        httpClient.send(
            HttpRequest.newBuilder(URI(logWebhookURL))
                .header("Content-Type", "application/json")
                .header("User-Agent", "EvoPlus/Service")
                .method("POST", HttpRequest.BodyPublishers.ofString("""{"content": "$text"}"""))
                .build(),
            BodyHandlers.discarding()
        )
    }

    @Scheduled(fixedDelay = 10, timeUnit = SECONDS)
    private fun updateStatus() {
        jda.presence.setPresence(Activity.watching("за ${playerService.count} игроками"), false)
    }

}