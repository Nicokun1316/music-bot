package io.github.nicokun1316.commands

import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import io.github.nicokun1316.MusicEnv
import io.github.nicokun1316.player

object ShuffleCommand: BotCommand("shuffle", "Shuffles") {
    context(env: MusicEnv)
    override suspend fun GuildChatInputCommandInteractionCreateEvent.execute(): String {
        val tracks = env.player.tracks
        tracks.shuffle()
        return "Queue ${tracks.currentIndex + 1}/${tracks.size}:\n${tracks.tableRep()}"
    }
}