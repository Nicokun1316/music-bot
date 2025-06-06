package io.github.nicokun1316

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import dev.kord.common.annotation.KordVoice
import dev.kord.core.behavior.channel.connect
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.voice.AudioFrame

context(event: GuildChatInputCommandInteractionCreateEvent)
@OptIn(KordVoice::class)
suspend fun MusicEnv.ensureConnected() {
    val vc = event.interaction.user.getVoiceStateOrNull()?.getChannelOrNull() ?: return
    if (hasPlayer) return
    player.connect(vc)
}

context(event: GuildChatInputCommandInteractionCreateEvent)
val MusicEnv.player: ServerPlayer
    get() = getPlayer(event.interaction.guild.id)

context(event: GuildChatInputCommandInteractionCreateEvent)
val MusicEnv.hasPlayer: Boolean
    get() = hasPlayer(event.interaction.guild.id)

context(event: GuildChatInputCommandInteractionCreateEvent)
suspend fun MusicEnv.disconnect() {
    disconnect(event.interaction.guild.id)
}

