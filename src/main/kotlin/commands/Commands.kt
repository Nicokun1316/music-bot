package io.github.nicokun1316.commands

import kotlin.reflect.full.memberFunctions

internal val commandsInternal = mutableListOf<BotCommand>()
val commands: Map<String, BotCommand> get() = commandsInternal.associateBy { it.name }

internal fun registerCommand(command: BotCommand) {
    commandsInternal.add(command)
}

fun initialiseCommands() {
    BotCommand::class.sealedSubclasses.forEach {
        it.memberFunctions.first {
            it.name == "initialise"
        }.call(
            it.objectInstance
        )
    }
}