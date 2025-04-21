package io.github.nicokun1316.commands

import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.rest.builder.interaction.GlobalChatInputCreateBuilder
import io.github.nicokun1316.MusicEnv

sealed class BotCommand(val name: String, val description: String, val needsVC: Boolean = true) {
    init {
        assert(name.all { it.isLowerCase() })
    }
    @Suppress("unused")
    fun initialise() {
        registerCommand(this)
    }

    context(env: MusicEnv)
    abstract suspend fun GuildChatInputCommandInteractionCreateEvent.execute(): String

    open fun GlobalChatInputCreateBuilder.builder() {}
}