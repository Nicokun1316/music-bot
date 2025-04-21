package io.github.nicokun1316.commands

import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import io.github.nicokun1316.MusicEnv
import io.github.nicokun1316.player

object StatusCommand: BotCommand("status", "Displays status") {
    context(env: MusicEnv)
    override suspend fun GuildChatInputCommandInteractionCreateEvent.execute(): String {
        val player = env.player
        val position = String.format("%.2f", (player.playingTrack?.position ?: 0) / 1000.0)
        val duration = String.format("%.2f", (player.playingTrack?.duration ?: 0) / 1000.0)
        val progress = String.format("%.0f", player.progress * 100)
        return "${position}s/${duration}s (${progress}%)"
    }
}