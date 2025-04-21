package io.github.nicokun1316.commands

import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.rest.builder.interaction.GlobalChatInputCreateBuilder
import dev.kord.rest.builder.interaction.string
import io.github.nicokun1316.MusicEnv
import io.github.nicokun1316.player

object PlayAllCommand: BotCommand("playall", "Plays the whole playlist") {
    context(env: MusicEnv)
    override suspend fun GuildChatInputCommandInteractionCreateEvent.execute(): String {
        val query = interaction.command.strings["query"] ?: return "No query specified"
        val tracks = env.findTracks(query, false)
        env.player.enqueue(tracks)
        return "Enqueued ${tracks.first().info.title}"
    }

    override fun GlobalChatInputCreateBuilder.builder() {
        string("query", "URL or query") {
            required = true
        }
    }
}