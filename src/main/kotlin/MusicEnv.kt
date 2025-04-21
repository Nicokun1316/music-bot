package io.github.nicokun1316

import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import dev.kord.common.entity.Snowflake
import dev.lavalink.youtube.YoutubeAudioSourceManager
import io.github.oshai.kotlinlogging.KotlinLogging

private val logger  = KotlinLogging.logger{}

class MusicEnv {
    constructor() {
        audioManager.registerSourceManager(ytSourceManager)
        AudioSourceManagers.registerRemoteSources(audioManager)
    }

    fun hasPlayer(snowflake: Snowflake): Boolean = snowflake in players

    fun getPlayer(snowflake: Snowflake): ServerPlayer = players.getOrPut(snowflake) {
        ServerPlayer(audioManager).apply { volume = 50 }
    }

    suspend fun disconnect(snowflake: Snowflake) {
        val player = players[snowflake]
        if (player != null) {
            player.disconnect()
            players.remove(snowflake)
        }
    }

    suspend fun findTracks(query: String, search: Boolean = true) = audioManager.findTracks(query, search)

    private val audioManager = DefaultAudioPlayerManager()
    private val ytSourceManager = YoutubeAudioSourceManager()
    private val players = mutableMapOf<Snowflake, ServerPlayer>()

    override fun toString(): String {
        return players.toString()
    }
}