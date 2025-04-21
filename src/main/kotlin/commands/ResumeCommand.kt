package io.github.nicokun1316.commands

import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import io.github.nicokun1316.MusicEnv
import io.github.nicokun1316.player

object ResumeCommand: BotCommand("Resume", "Resumes the bot") {
    context(env: MusicEnv)
    override suspend fun GuildChatInputCommandInteractionCreateEvent.execute(): String {
        env.player.resume()
        return "Resumed"
    }
}