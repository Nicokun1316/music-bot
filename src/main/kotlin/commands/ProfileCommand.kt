package io.github.nicokun1316.commands

import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.rest.builder.interaction.GlobalChatInputCreateBuilder
import dev.kord.rest.builder.interaction.user
import io.github.nicokun1316.MusicEnv
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.flow.toList

private val logger = KotlinLogging.logger {}

object ProfileCommand: BotCommand("profile", "get avatar of chosen member") {
    context(env: MusicEnv)
    override suspend fun GuildChatInputCommandInteractionCreateEvent.execute(): String {
        val target = interaction.command.users["target"]?.let { listOf(it) }
        val members = target ?: interaction.guild.members.toList()
        logger.debug { "Getting profiles of ${members.size} members" }
        val urls = members.mapNotNull { it.avatar?.cdnUrl?.toUrl() }
        return urls.joinToString(" ")
    }

    override fun GlobalChatInputCreateBuilder.builder() {
        user("target", "Whose pfp do you want") {
            required = false
        }
    }
}