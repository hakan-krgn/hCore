package com.hakan.core.packet;

import com.hakan.core.HCore;
import com.hakan.core.listener.HListenerAdapter;
import com.hakan.core.packet.listeners.PlayerConnectionListener;
import com.hakan.core.packet.player.HPacketPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * HPacketHandler class.
 */
public final class HPacketHandler {

    private static final Map<Player, HPacketPlayer> PACKET_PLAYERS = new HashMap<>();
    private static Class<?> PACKET_CLASS;

    /**
     * Initializes the packet system.
     *
     * @param plugin Main class of plugin.
     */
    public static void initialize(@Nonnull JavaPlugin plugin) {
        try {

            Class<?> clazz = Class.forName("com.hakan.core.packet.player.HPacketPlayer_" + HCore.getVersionString());
            if (HPacketPlayer.class.isAssignableFrom(clazz)) {
                HPacketHandler.PACKET_CLASS = clazz;
            }

            HListenerAdapter.register(new PlayerConnectionListener(plugin));
        } catch (Exception e) {
            e.printStackTrace();
            plugin.getLogger().warning("Could not initialize packet system. Probably you are using an unsupported version(" + HCore.getVersionString() + ")");
        }

    }


    /**
     * Gets content as safe.
     *
     * @return Content.
     */
    @Nonnull
    public static Map<Player, HPacketPlayer> getContentSafe() {
        return new HashMap<>(HPacketHandler.PACKET_PLAYERS);
    }

    /**
     * Gets content.
     *
     * @return Content.
     */
    @Nonnull
    public static Map<Player, HPacketPlayer> getContent() {
        return HPacketHandler.PACKET_PLAYERS;
    }

    /**
     * Gets values as safe.
     *
     * @return Values.
     */
    @Nonnull
    public static Collection<HPacketPlayer> getValuesSafe() {
        return new ArrayList<>(HPacketHandler.PACKET_PLAYERS.values());
    }

    /**
     * Gets values.
     *
     * @return Values.
     */
    @Nonnull
    public static Collection<HPacketPlayer> getValues() {
        return HPacketHandler.PACKET_PLAYERS.values();
    }

    /**
     * Registers the player listener.
     *
     * @param player Player.
     */
    public static void register(@Nonnull Player player) {
        try {
            Objects.requireNonNull(player, "player cannot be null!");

            HPacketPlayer packetPlayer = (HPacketPlayer) HPacketHandler.PACKET_CLASS.getConstructor(Player.class).newInstance(player);
            HPacketHandler.PACKET_PLAYERS.put(player, packetPlayer);

            packetPlayer.register();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Unregisters the player listener.
     *
     * @param player Player.
     */
    public static void unregister(@Nonnull Player player) {
        Objects.requireNonNull(player, "player cannot be null!");
        HPacketPlayer packetPlayer = HPacketHandler.PACKET_PLAYERS.remove(player);
        if (packetPlayer != null)
            packetPlayer.unregister();
    }
}