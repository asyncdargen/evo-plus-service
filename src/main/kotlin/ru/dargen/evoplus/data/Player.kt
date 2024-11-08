package ru.dargen.evoplus.data

import java.time.Duration
import java.time.Instant

data class Player(
    val name: String, var version: String,
    var timestamp: Instant = Instant.now(), val joinTimestamp: Instant = timestamp,
) {

    val onlineTime get() = Duration.between(joinTimestamp, timestamp)

    fun update(version: String) {
        timestamp = Instant.now()
        this.version = version
    }

}