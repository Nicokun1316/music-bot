package io.github.nicokun1316.commands

import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import io.github.nicokun1316.MusicEnv
import io.github.nicokun1316.player
import kotlin.math.min

object QueueCommand: BotCommand("queue", "Shows the current queue") {
    context(env: MusicEnv)
    override suspend fun GuildChatInputCommandInteractionCreateEvent.execute(): String {
        val player = env.player
        val queue = player.queue
        return "Queue ${min(player.currentIndex + 1, queue.size)}/${queue.size}" + queue.joinToString("\n") { it.info.title }
    }
}