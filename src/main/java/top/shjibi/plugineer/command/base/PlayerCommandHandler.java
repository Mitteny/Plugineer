package top.shjibi.plugineer.command.base;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.shjibi.plugineer.command.CommandManager;
import top.shjibi.plugineer.command.base.annotations.CommandInfo;

import java.util.Collections;
import java.util.List;

/**
 * A command handler for player-only commands
 */
public abstract class PlayerCommandHandler extends CommandHandler {

    protected final boolean silent;

    protected final String[] playerOnlyMsg;

    /**
     * Constructs a player command handler for executing commands and completing tabs
     */
    public PlayerCommandHandler() {
        super();
        CommandInfo info = getClass().getAnnotationsByType(CommandInfo.class)[0];
        this.silent = info.silent();
        this.playerOnlyMsg = info.playerOnlyMsg();
    }

    @Override
    public final List<String> completeTab(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) return Collections.emptyList();
        return completeTab((Player) sender, command, label, args);
    }

    @Override
    public final void execute(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(playerOnlyMsg);
            return;
        }
        execute((Player) sender, command, label, args);
    }

    public List<String> completeTab(@NotNull Player p, @Nullable Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }

    public abstract void execute(@NotNull Player p, @Nullable Command command, @NotNull String label, @NotNull String[] args);

    @Override
    public void bind(@NotNull CommandManager manager) {
        if (silent) {
            manager.getSilentCommandListener().addSilentHandler(this);
        } else {
            super.bind(manager);
        }
    }

    /**
     * Gets the player-only message.
     */
    public String[] getPlayerOnlyMsg() {
        return playerOnlyMsg;
    }
}
