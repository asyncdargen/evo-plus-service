package ru.dargen.evoplus.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.dargen.evoplus.service.PlayerService

@RestController
@RequestMapping("/api/ingame/")
class GameController(private val playerService: PlayerService) {

    @get:GetMapping("/list")
    val players get() = ResponseEntity.ok(playerService.players)
    @get:GetMapping("/count")
    val count get() = ResponseEntity.ok(playerService.playersCount)

    @PostMapping("/update")
    fun updateIngame(@RequestParam("username") username: String, @RequestParam("version") version: String): ResponseEntity<Boolean> {
        playerService.updateIngame(username, version)
        return ResponseEntity.ok(true)
    }

    @GetMapping("/check")
    fun checkIngame(@RequestParam("username") username: String): ResponseEntity<Boolean> {
        return ResponseEntity.ok(playerService.isIngame(username))
    }

    @PostMapping("/check/batch")
    fun checkIngame(@RequestBody usernames: Collection<String>): ResponseEntity<Collection<String>> {
        return ResponseEntity.ok(playerService.filterIngame(usernames))
    }

}