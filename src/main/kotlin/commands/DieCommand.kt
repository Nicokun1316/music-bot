package io.github.nicokun1316.commands

import dev.kord.core.behavior.channel.connect
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import io.github.nicokun1316.MusicEnv
import io.github.nicokun1316.disconnect
import io.github.nicokun1316.player

object DieCommand: BotCommand("die", "Kills the bot") {
    context(env: MusicEnv)
    override suspend fun GuildChatInputCommandInteractionCreateEvent.execute(): String {
        env.disconnect()
        return "Died"
    }
}