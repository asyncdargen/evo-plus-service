package ru.dargen.evoplus.configuration

import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.requests.GatewayIntent
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DiscordConfiguration {

    @Value("\${discord.token}")
    lateinit var token: String

    @get:Bean("discord.webhook.log")
    @Value("\${discord.webhook.log}")
    lateinit var webhook: String

    @get:Bean
    val discord get() = JDABuilder.createLight(token, GatewayIntent.entries).build()

}