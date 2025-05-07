package io.github.nicokun1316

import dev.kord.common.Color
import dev.kord.common.annotation.KordVoice
import dev.kord.core.Kord
import dev.kord.core.behavior.channel.createMessage
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.on
import dev.kord.gateway.Intent
import dev.kord.gateway.PrivilegedIntent
import dev.kord.rest.builder.message.embed
import io.github.cdimascio.dotenv.dotenv
import io.github.nicokun1316.commands.commands
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.runBlocking
import io.github.nicokun1316.commands.*

private val logger = KotlinLogging.logger {}

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

    initialiseCommands()

    kord.on<MessageCreateEvent> {
        if (message.author?.isBot == true) return@on
        if (message.content.contains("meow", true)) {
            message.channel.createMessage("meeoowww")
            message.channel.createMessage {
                embed {
                    title = "Title"
                    description = "This is a long description"
                    author { name = "author" }
                    color = Color(0x00FF00)
                    footer {
                        text = "footer"
                    }
                    field {
                        name = "Field inline"
                        value = "Field value inline"
                        inline = true
                    }
                    field {
                        name = "Field not inline"
                        value = "Field value not inline"
                        inline = false
                    }
                }
            }
        }
    }

    kord.createCommands()

    kord.on<GuildChatInputCommandInteractionCreateEvent> {
        val response = interaction.deferPublicResponse()
        val command = commands[interaction.invokedCommandName]
        if (command == null) {
            logger.warn {  "No command found for name: ${interaction.invokedCommandName}" }
            response.respond { content = "Unknown command ${interaction.invokedCommandName}" }
            return@on
        }
        try {
            with(env) {
                logger.info { "Running command ${interaction.invokedCommandName}" }
                if (command.needsVC) {
                    logger.info { "Ensuring VC" }
                    ensureConnected()
                }
                val responseString = with(command) { execute() }
                response.respond { content = responseString }
            }
        } catch (e: Exception) {
            logger.warn(e) { "Command ${interaction.invokedCommandName} failed to execute" }
            response.respond { content = e.message ?: "Command  ${interaction.invokedCommandName} failed" }
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