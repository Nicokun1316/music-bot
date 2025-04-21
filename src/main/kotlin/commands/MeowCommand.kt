package io.github.nicokun1316.commands

import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import io.github.nicokun1316.MusicEnv
import io.github.nicokun1316.player

object MeowCommand: BotCommand("meow", "Meow") {
    context(env: MusicEnv)
    override suspend fun GuildChatInputCommandInteractionCreateEvent.execute(): String {
        val tracks = env.findTracks("it's a whole world kagamine len")
        env.player.enqueue(tracks.first())
        return "I no longer think"
    }
}