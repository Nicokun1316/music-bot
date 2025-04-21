package io.github.nicokun1316

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import dev.kord.common.annotation.KordVoice
import dev.kord.core.Kord
import dev.kord.core.behavior.channel.BaseVoiceChannelBehavior
import dev.kord.core.behavior.channel.connect
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.on
import dev.kord.gateway.Intent
import dev.kord.gateway.PrivilegedIntent
import dev.kord.rest.builder.interaction.string
import dev.kord.voice.AudioFrame
import io.github.cdimascio.dotenv.dotenv
import kotlinx.coroutines.runBlocking




//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
@OptIn(KordVoice::class)
suspend fun main() {
    val dotenv = dotenv()
    val appId = dotenv["APP_ID"]
    val publicKey = dotenv["PUBLIC_KEY"]
    val secret = dotenv["BOT_TOKEN"]

    val kord = Kord(secret)

    val env = MusicEnv()

    kord.on<MessageCreateEvent> {
        if (message.author?.isBot == true) return@on
        if (message.content.contains("meow", true)) {
            message.channel.createMessage("meeoowww")
        }
    }

    val meowCommand = kord.createGlobalChatInputCommand("meow", "meow meow") {

    }

    val playCommand = kord.createGlobalChatInputCommand("play", "meowww") {
        string("track", "song name/url or playlist url") {
            required = true
        }
    }

    kord.on<GuildChatInputCommandInteractionCreateEvent> {
        val response = interaction.deferPublicResponse()
        if (interaction.invokedCommandName == "meow") {
            env.ensureConnected()
            try {
                val tracks = env.findTracks("it's a whole world kagamine len")
                env.player.enqueue(tracks)
                response.respond { content = "I no longer think" }
            } catch (e: Exception) {
                response.respond { content = "I am error ${e.message}" }
            }
        } else if (interaction.invokedCommandName == "play") {
            val query = interaction.command.strings["track"] ?: return@on
            try {
                val tracks = env.findTracks(query)
                env.player.enqueue(tracks)
                response.respond { content = "Enqueued ${tracks.size} tracks" }
            } catch (e: Exception) {
                response.respond { content = "I am error ${e.message}" }
            }
        }
    }

    Runtime.getRuntime().addShutdownHook(Thread {
        runBlocking {
            kord.shutdown()
        }
    })

    kord.login {
        @OptIn(PrivilegedIntent::class)
        intents += Intent.MessageContent
    }
}