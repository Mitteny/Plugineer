package top.shjibi.plugineer.command.base;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.NotNull;
import top.shjibi.plugineer.command.base.annotations.CommandInfo;

import java.util.Collections;
import java.util.List;

/**
 * A command handler for console-only commands
 */
public abstract class ConsoleCommandHandler extends CommandHandler {

    protected final String[] consoleOnlyMsg;

    /**
     * Constructs a player command handler for executing commands and completing tabs
     */
    public ConsoleCommandHandler() {
        super();
        CommandInfo info = getClass().getAnnotationsByType(CommandInfo.class)[0];
        this.consoleOnlyMsg = info.consoleOnlyMsg();
    }

    @Override
    public final List<String> completeTab(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof ConsoleCommandSender)) return Collections.emptyList();
        return completeTab((ConsoleCommandSender) sender, command, label, args);
    }

    @Override
    public final void execute(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof ConsoleCommandSender)) {
            sender.sendMessage(consoleOnlyMsg);
            return;
        }
        execute((ConsoleCommandSender) sender, command, label, args);
    }

    public List<String> completeTab(@NotNull ConsoleCommandSender consoleSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }

    public abstract void execute(@NotNull ConsoleCommandSender consoleSender, @NotNull Command command, @NotNull String label, @NotNull String[] args);

    /**
     * Gets the console-only message
     */
    public String[] getConsoleOnlyMsg() {
        return consoleOnlyMsg;
    }
}
