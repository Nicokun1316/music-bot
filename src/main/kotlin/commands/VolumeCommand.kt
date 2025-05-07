package io.github.nicokun1316.commands

import dev.kord.core.behavior.interaction.respondEphemeral
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.rest.builder.interaction.GlobalChatInputCreateBuilder
import dev.kord.rest.builder.interaction.integer
import io.github.nicokun1316.MusicEnv
import io.github.nicokun1316.player
import io.github.oshai.kotlinlogging.KotlinLogging

private val logger = KotlinLogging.logger {}

object VolumeCommand: BotCommand("volume", "Sets or displays volume (0-100)") {
    context(env: MusicEnv)
    override suspend fun GuildChatInputCommandInteractionCreateEvent.execute(): String {
        val value = interaction.command.integers["value"]
        val player = env.player
        logger.debug { "Setting volume to: $value" }
        value?.let {
            logger.debug { "Factually setting volume" }
            player.volume = it.toInt()
        }

        return "Volume is set to ${player.volume}"
    }

    override fun GlobalChatInputCreateBuilder.builder() {
        integer("value", "(0-100)")
    }
}