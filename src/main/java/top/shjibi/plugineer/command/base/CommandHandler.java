package top.shjibi.plugineer.command.base;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.shjibi.plugineer.command.CommandManager;
import top.shjibi.plugineer.command.base.annotations.CommandInfo;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>A basic command handler.
 * <br>You implement it and register it to a {@link CommandManager} to handle one or multiple specific commands.
 * <br>It's important to know that this is an annotation-based class and you'll have to annotate it with {@link CommandInfo} in order to register it to a {@link CommandManager}.
 * </p>
 */
public abstract class CommandHandler implements TabExecutor {
    @NotNull
    protected final String[] names;
    protected final int minArgs;

    /**
     * Constructs a command handler for executing commands and completing tabs
     */
    public CommandHandler() {
        CommandInfo[] infoArray = getClass().getAnnotationsByType(CommandInfo.class);
        if (infoArray.length == 0) throw new RuntimeException("CommandInfo is not found!");
        CommandInfo info = infoArray[0];

        this.names = Arrays.stream(info.name()).map(s -> s.toLowerCase(Locale.ENGLISH)).toArray(String[]::new);
        this.minArgs = info.minArgs();
    }

    /**
     * Binds this handler to the corresponding commands.
     *
     * @param manager the manager that works with this command handler
     */
    public void bind(@NotNull CommandManager manager) {
        JavaPlugin plugin = manager.getPlugin();
        for (String name : names) {
            PluginCommand command = Objects.requireNonNull(plugin.getCommand(name));
            command.setExecutor(this);
            command.setTabCompleter(this);
        }
    }

    /**
     * Sends the correct usage of the command
     */
    protected final void sendUsage(@NotNull Command command, @NotNull CommandSender sender, @NotNull String commandLabel) {
        String usageMessage = command.getUsage();
        if (!usageMessage.isEmpty()) {
            for (String line : usageMessage.replace("<command>", commandLabel).split("\n")) {
                sender.sendMessage(line);
            }
        }
    }

    @Override
    public final boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length < minArgs) {
            sendUsage(command, sender, label);
            return true;
        }
        execute(sender, command, label, args);
        return true;
    }

    @Override
    public final List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        return completeTab(sender, command, label, args);
    }

    /**
     * Handles the tab list
     */
    @Nullable
    public List<String> completeTab(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }

    /**
     * Handles the command
     */
    public abstract void execute(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args);

    /**
     * Gets the names of commands associated with this handler
     */
    @NotNull
    public String[] getNames() {
        return names;
    }

    /**
     * Gets the minimum argument count required for the command to run.
     */
    public int getMinArgs() {
        return minArgs;
    }

    /**
     * @param command the given command
     * @return whether the given command applies to this handler
     */
    public boolean isCommandApplicable(@NotNull String command) {
        return Arrays.stream(names).
                anyMatch(name -> name.equals(command.toLowerCase(Locale.ENGLISH)));
    }
}
