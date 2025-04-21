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

    fun hasPlayer(snowflake: Snowflake): Boolean {
        logger.debug { "hasPlayer $snowflake" }
        return snowflake in players
    }

    fun getPlayer(snowflake: Snowflake): ServerPlayer {
        logger.debug { "getPlayer $snowflake" }
        return players.getOrPut(snowflake) {
            ServerPlayer(audioManager).apply { volume = 50 }
        }
    }

    suspend fun findTracks(query: String) = audioManager.findTracks(query)

    private val audioManager = DefaultAudioPlayerManager()
    private val ytSourceManager = YoutubeAudioSourceManager()
    private val players = mutableMapOf<Snowflake, ServerPlayer>()

    override fun toString(): String {
        return players.toString()
    }
}