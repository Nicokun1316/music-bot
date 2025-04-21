package io.github.nicokun1316.commands

internal val commandsInternal = mutableListOf<BotCommand>()
val commands: Map<String, BotCommand> = commandsInternal.associateBy { it.name }

internal fun registerCommand(command: BotCommand) {
    commandsInternal.add(command)
}