package top.shjibi.plugineer.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;

/**
 * A deprecated utility class that makes NMS and CraftBukkit easier.
 */
@Deprecated
public final class NMSUtil {

    private NMSUtil() {
    }

    /**
     * Gets the version of Minecraft that the server is running on
     */
    @NotNull
    public static String getVersion() {
        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        return packageName.substring(packageName.lastIndexOf(".") + 1);
    }

    /**
     * Gets a class in net.minecraft package
     */
    @Nullable
    public static Class<?> getNMClass(@NotNull String name) {
        try {
            return Class.forName("net.minecraft." + name);
        } catch (ClassNotFoundException e) {
            Bukkit.getLogger().log(Level.WARNING, "Cannot get NM class: " + name + "!");
            return null;
        }
    }

    /**
     * Gets a NMS class
     */
    @Nullable
    public static Class<?> getNMSClass(@NotNull String name) {
        try {
            return Class.forName("net.minecraft.server." + getVersion() + "." + name);
        } catch (ClassNotFoundException e) {
            Bukkit.getLogger().log(Level.WARNING, "Cannot find NMS class: " + name + "!");
            return null;
        }
    }

    /**
     * Gets a CraftBukkit class
     */
    @Nullable
    public static Class<?> getCraftBukkitClass(@NotNull String name) {
        try {
            return Class.forName("org.bukkit.craftbukkit." + getVersion() + "." + name);
        } catch (ClassNotFoundException e) {
            Bukkit.getLogger().log(Level.WARNING, "Cannot find CraftBukkit class: " + name + "!");
            return null;
        }
    }

    /**
     * Sends a packet to the player
     */
    public static boolean sendPacket(@NotNull Player p, @NotNull Object packet) {
        try {
            Class<?> packetClass = Class.forName("net.minecraft.network.protocol.Packet");
            if (!packetClass.isAssignableFrom(packet.getClass())) return false;
            Object entityPlayer = p.getClass().getMethod("getHandle").invoke(p);
            Object connection = entityPlayer.getClass().getField("b").get(entityPlayer);
            connection.getClass().getMethod("a", packetClass).invoke(connection, packet);
            return true;
        } catch (ReflectiveOperationException e) {
            Bukkit.getLogger().log(Level.WARNING, "Cannot send packet: " + packet.getClass().getSimpleName());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Gets an NMS copy of the {@link ItemStack}
     */
    @Nullable
    public static Object asNMSCopy(@NotNull ItemStack item) {
        try {
            Class<?> itemClass = getCraftBukkitClass("inventory.CraftItemStack");
            if (itemClass == null) throw new ReflectiveOperationException();
            return itemClass.getMethod("asNMSCopy", ItemStack.class).invoke(null, item);
        } catch (ReflectiveOperationException e) {
            Bukkit.getLogger().log(Level.WARNING, "Cannot get NMS copy of item!");
            e.printStackTrace();
            return null;
        }
    }
}
