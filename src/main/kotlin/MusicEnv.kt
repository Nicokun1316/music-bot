package io.github.nicokun1316

import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import dev.kord.common.entity.Snowflake
import dev.lavalink.youtube.YoutubeAudioSourceManager

class MusicEnv {
    constructor() {
        audioManager.registerSourceManager(ytSourceManager)
        AudioSourceManagers.registerRemoteSources(audioManager)
    }

    fun hasPlayer(snowflake: Snowflake): Boolean = snowflake in players

    fun getPlayer(snowflake: Snowflake): ServerPlayer = players.getOrPut(snowflake) {
        ServerPlayer(audioManager)
    }

    suspend fun findTracks(query: String) = audioManager.findTracks(query)

    private val audioManager = DefaultAudioPlayerManager()
    private val ytSourceManager = YoutubeAudioSourceManager()
    private val players = mutableMapOf<Snowflake, ServerPlayer>()
}