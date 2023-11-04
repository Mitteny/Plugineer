package top.shjibi.plugineer.command.base.annotations;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import top.shjibi.plugineer.command.base.ConsoleCommandHandler;
import top.shjibi.plugineer.command.base.PlayerCommandHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Information of a command handler
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CommandInfo {
    /**
     * Names of command(s) that the handler handles
     */
    String[] name();

    /**
     * If a command is silent, you can handle it without needing to register it to the server. Only a command executed by players can be silent.
     */
    boolean silent();

    /**
     * Minimum argument count for the command(s) to run
     */
    int minArgs() default 0;

    /**
     * Message to send when a non {@link Player} executed the command (Only available for {@link PlayerCommandHandler})
     */
    String[] playerOnlyMsg() default "&cThis command can only be executed by players!";

    /**
     * Message to send when a non {@link ConsoleCommandSender} executed the command (Only available for {@link ConsoleCommandHandler})
     */
    String[] consoleOnlyMsg() default "&cThis command can only be executed by a console!";
}
