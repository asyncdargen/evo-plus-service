package ru.dargen.evoplus.service

import ru.dargen.evoplus.data.Player

interface PlayerService {

    val players: Collection<Player>
    val playersCount: Int

    fun updateIngame(username: String, version: String)

    fun isIngame(username: String): Boolean

    fun filterIngame(usernames: Collection<String>): Collection<String>

}