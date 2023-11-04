package top.shjibi.plugineer.command;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.shjibi.plugineer.command.base.CommandHandler;
import top.shjibi.plugineer.command.base.annotations.RegisterCommand;
import top.shjibi.plugineer.command.base.listener.SilentCommandListener;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * A class that helps you manage your commands.
 */
public final class CommandManager {

    @SuppressWarnings("unchecked")
    private CommandManager(@NotNull JavaPlugin plugin) {
        Preconditions.checkNotNull(plugin, "'plugin' cannot be null!");
        this.plugin = plugin;
        commandHandlers = new ArrayList<>();
        silentCommandListener = SilentCommandListener.get(plugin);

        try {
            Server server = Bukkit.getServer();
            Field commandMapField = server.getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            commandMap = (SimpleCommandMap) commandMapField.get(server);

            Field knownCommandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");
            knownCommandsField.setAccessible(true);
            knownCommands = (Map<String, Command>) knownCommandsField.get(commandMap);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Cannot get commandMap or knownCommands", e);
        }
    }


    @Nullable
    private static CommandManager instance;
    @NotNull
    private final JavaPlugin plugin;
    @NotNull
    private final List<CommandHandler> commandHandlers;
    @NotNull
    private final SilentCommandListener silentCommandListener;
    @NotNull
    private final Map<String, Command> knownCommands;
    @NotNull
    private final SimpleCommandMap commandMap;

    /**
     * Gets or create the only instance of {@link CommandManager} for a plugin.
     *
     * @return the instance of {@link CommandManager} for a plugin
     */
    @NotNull
    public static CommandManager get(JavaPlugin plugin) {
        if (instance == null) instance = new CommandManager(plugin);
        return instance;
    }

    /**
     * Adds and binds all the provided command handlers, if the given handler is annotated by {@link RegisterCommand}, also registers the commands to the server.
     *
     * @param handlerClasses handler classes to add
     */
    public void addHandlers(@NotNull Class<? extends CommandHandler>[] handlerClasses) {
        for (Class<? extends CommandHandler> clazz : handlerClasses) {
            try {
                CommandHandler handler = clazz.getConstructor(JavaPlugin.class).newInstance(plugin);
                List<String> nameList = Lists.newArrayList(handler.getNames());

                for (RegisterCommand info : handler.getClass().getAnnotationsByType(RegisterCommand.class)) {
                    String name = info.name().toLowerCase(Locale.ENGLISH);
                    if (nameList.contains(name)) {
                        registerCommand(info.force(),
                                info.name(),
                                info.description(),
                                info.usageMessage(),
                                info.aliases());
                    }
                }

                handler.bind(this);
                commandHandlers.add(handler);
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException("Cannot bind command handler: " + clazz.getSimpleName(), e);
            }
        }
    }

    /**
     * Registers a command using the given name, description and aliases
     *
     * @param force       If a command with the same name is already registered, whether to replace the old one with this
     * @param name        Name of the command
     * @param description Description of the command
     * @param usage       Usage of the command
     * @param aliases     Aliases of the command
     * @return the registered command or null if it was unsuccessful to register the command
     */
    public Command registerCommand(boolean force, @NotNull String name, @Nullable String description, @Nullable String usage, @Nullable String... aliases) {
        try {
            Constructor<PluginCommand> pluginCommandConstructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            pluginCommandConstructor.setAccessible(true);

            PluginCommand command = pluginCommandConstructor.newInstance(name, plugin);
            command.setDescription(description == null ? "" : description);
            command.setAliases(aliases == null ? Collections.emptyList() : Lists.newArrayList(aliases));
            command.setUsage(usage == null ? "" : usage);

            boolean result = commandMap.register(plugin.getName(), command);

            if (!result) {
                if (!force) {
                    return null;
                }
                unregisterCommand(name);
                commandMap.register(plugin.getName(), command);
                return commandMap.getCommand("name");
            }
            return commandMap.getCommand("name");
        } catch (ReflectiveOperationException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot register command: " + name);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Unregisters commands with the given name
     *
     * @param name Name of the command to unregister
     */
    public void unregisterCommand(String name) {
        Set<Map.Entry<String, Command>> entrySet = knownCommands.entrySet();
        entrySet.stream()
                .filter(x -> x.getValue().getName().equals(name))
                .peek(x -> x.getValue().unregister(commandMap))
                .collect(Collectors.toList()).forEach(entrySet::remove);
    }

    /**
     * Gets an immutable copy of command handler list.
     *
     * @return am immutable copy of command handler list
     */
    @NotNull
    public List<CommandHandler> getCommandHandlers() {
        return new ArrayList<>(commandHandlers);
    }

    /**
     * Gets the silent command handler
     *
     * @return the silent command handler
     */
    @NotNull
    public SilentCommandListener getSilentCommandListener() {
        return silentCommandListener;
    }

    /**
     * Adds a simple command to the server with the given arguments
     *
     * @param force       If a command with the same name is already registered, whether to replace the old one with this
     * @param name        Name of the command
     * @param description Description of the command
     * @param usage       Usage of the command
     * @param aliases     Aliases of the command
     * @param executor    Executor that associates with the command
     * @param completer   Completer that associates with the command
     * @return the registered command or null if it was unsuccessful to register the command
     */
    public Command addSimpleCommand(boolean force, @NotNull String name, @Nullable String description, @Nullable String usage, @Nullable String[] aliases, @NotNull CommandExecutor executor, @Nullable TabCompleter completer) {
        Preconditions.checkNotNull(name, "Command name cannot be null!");
        Preconditions.checkNotNull(executor, "Command executor cannot be null!");
        Command command = registerCommand(force, name, description, usage, aliases);
        if (command == null) return null;
        ((PluginCommand) command).setExecutor(executor);
        ((PluginCommand) command).setTabCompleter(completer);
        return command;
    }

    /**
     * Gets the {@link CommandHandler} instance based on the given class
     *
     * @return The {@link CommandHandler} instance based on the given class
     */
    @Nullable
    public CommandHandler getCommandHandler(Class<? extends CommandHandler> clazz) {
        for (CommandHandler handler : commandHandlers) {
            if (handler.getClass() == clazz) return handler;
        }
        return null;
    }

    /**
     * Gets an immutable copy of the registered commands on this server
     *
     * @return An immutable copy of the registered commands on this server
     */
    @NotNull
    public Map<String, Command> getKnownCommands() {
        return Maps.newHashMap(knownCommands);
    }

    /**
     * Gets the plugin that this command manager works with.
     *
     * @return the plugin that this command manager works with
     */
    public JavaPlugin getPlugin() {
        return plugin;
    }
}
