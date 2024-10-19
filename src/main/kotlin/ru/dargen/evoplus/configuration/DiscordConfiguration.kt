package ru.dargen.evoplus.configuration

import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.requests.GatewayIntent
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy

@Configuration
class DiscordConfiguration {

    @Value("\${discord.token}")
    private lateinit var token: String

    @get:Lazy
    @get:Bean
    val jda get() = JDABuilder.createLight(token, GatewayIntent.entries).build()

}