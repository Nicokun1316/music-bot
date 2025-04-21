package io.github.nicokun1316.commands

import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import io.github.nicokun1316.MusicEnv
import io.github.nicokun1316.player

object StatusCommand: BotCommand("status", "Displays status") {
    context(env: MusicEnv)
    override suspend fun GuildChatInputCommandInteractionCreateEvent.execute(): String {
        val player = env.player
        return "${(player.playingTrack?.position ?: 0) / 1000.0}s/${(player.playingTrack?.duration ?: 0) / 1000.0}s (${player.progress * 100}%)"
    }
}