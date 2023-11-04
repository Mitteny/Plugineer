package top.shjibi.plugineer.command.base.listener;

import com.google.common.base.Preconditions;
import org.bukkit.command.CommandException;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.shjibi.plugineer.command.base.PlayerCommandHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SilentCommandListener implements Listener {

    private static SilentCommandListener instance;
    private final JavaPlugin plugin;
    private final List<PlayerCommandHandler> silentHandlers = new ArrayList<>();

    private SilentCommandListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Gets or create the only instance of {@link SilentCommandListener} for a plugin.
     *
     * @return the instance of {@link SilentCommandListener} for a plugin
     */
    @NotNull
    public static SilentCommandListener get(@NotNull JavaPlugin plugin) {
        Preconditions.checkNotNull(plugin, "'plugin' cannot be null!");
        if (instance == null) instance = new SilentCommandListener(plugin);
        return instance;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent e) {
        String[] split = e.getMessage().split(" ");
        String label = split[0];
        PlayerCommandHandler handler = getSilentHandler(label);

        if (handler == null) return;

        Player sender = e.getPlayer();
        e.setCancelled(true);
        String[] args = new String[split.length - 1];
        System.arraycopy(split, 1, args, 0, args.length);

        try {
            handler.execute(sender, null, label, args);
        } catch (Throwable ex) {
            sender.sendMessage(org.bukkit.ChatColor.RED + "An internal error occurred while attempting to perform this command");
            throw new CommandException("Unhandled exception executing command '" + label + "' in plugin " + plugin.getDescription().getFullName(), ex);
        }
    }

    /**
     * Adds a handler to the silent handler list
     *
     * @param handler the handler to add
     */
    public void addSilentHandler(@NotNull PlayerCommandHandler handler) {
        silentHandlers.add(handler);
    }

    /**
     * Returns an immutable copy of silent handlers.
     *
     * @return an immutable copy of silent handlers
     */
    @NotNull
    public List<PlayerCommandHandler> getSilentHandlers() {
        return new ArrayList<>(silentHandlers);
    }

    /**
     * Gets the associated silent handler for a silent command
     *
     * @param label The given command
     * @return the {@link PlayerCommandHandler} associated with this command or null if there isn't one
     */
    @Nullable
    public PlayerCommandHandler getSilentHandler(String label) {
        Optional<PlayerCommandHandler> optional = silentHandlers.stream()
                .filter(handler -> handler.isCommandApplicable(label)).findAny();
        return optional.orElse(null);
    }

}
