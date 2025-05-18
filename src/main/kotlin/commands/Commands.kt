package io.github.nicokun1316.commands

import dev.kord.core.Kord
import io.github.oshai.kotlinlogging.KotlinLogging
import java.util.prefs.Preferences
import kotlin.reflect.full.memberFunctions

internal val commandsInternal = mutableListOf<BotCommand>()
val commands: Map<String, BotCommand> get() = commandsInternal.associateBy { it.name }

internal fun registerCommand(command: BotCommand) {
    commandsInternal.add(command)
}

private val logger = KotlinLogging.logger {}

fun initialiseCommands() {
    BotCommand::class.sealedSubclasses.forEach {
        it.memberFunctions.first { function ->
            function.name == "initialise"
        }.call(
            it.objectInstance
        )
    }
}

suspend fun Kord.createCommands() {
    val preferences = Preferences.userNodeForPackage(BotCommand::class.java)

    val timestamps = object {}.javaClass.getResourceAsStream("/commands.yaml")?.bufferedReader()?.use { reader ->
        reader.readLines().associate {
            val parts = it.split(":").map(String::trim)
            val timestamp = parts[1].toLong()
            parts[0] to timestamp
        }
    } ?: emptyMap()

    for (command in commands.values) {
        val commandClass = command::class
        val name = commandClass.simpleName
        if (timestamps[name] == preferences.getLong(name, 0)) {
            logger.info { "Command $name already created" }
        } else {
            timestamps[name]?.let { timestamp -> preferences.putLong(name, timestamp) }
            createGlobalChatInputCommand(command.name, command.description) { with(command) { builder() } }
        }
    }
    preferences.flush()
    preferences.sync()
}