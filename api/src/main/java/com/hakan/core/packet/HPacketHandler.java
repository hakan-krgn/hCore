package com.hakan.core.packet;

import com.hakan.core.HCore;
import com.hakan.core.packet.listeners.PlayerConnectionListener;
import com.hakan.core.packet.player.HPacketPlayer;
import org.bukkit.Bukkit;
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

    private static Class<?> packetManagerClass;
    private static final Map<Player, HPacketPlayer> packetPlayers = new HashMap<>();

    /**
     * Initializes the packet system.
     */
    public static void initialize() {
        try {
            Class<?> clazz = Class.forName("com.hakan.core.packet.player.HPacketPlayer_" + HCore.getVersionString());
            if (HPacketPlayer.class.isAssignableFrom(clazz)) {
                HPacketHandler.packetManagerClass = clazz;
            }

            HCore.registerListeners(new PlayerConnectionListener());
        } catch (Exception e) {
            Bukkit.getLogger().warning("Could not initialize packet system. Probably you are using an unsupported version(" + HCore.getVersionString() + ")");
            e.printStackTrace();
        }
    }


    /**
     * Gets content as safe.
     *
     * @return Content.
     */
    @Nonnull
    public static Map<Player, HPacketPlayer> getContentSafe() {
        return new HashMap<>(HPacketHandler.packetPlayers);
    }

    /**
     * Gets content.
     *
     * @return Content.
     */
    @Nonnull
    public static Map<Player, HPacketPlayer> getContent() {
        return HPacketHandler.packetPlayers;
    }

    /**
     * Gets values as safe.
     *
     * @return Values.
     */
    @Nonnull
    public static Collection<HPacketPlayer> getValuesSafe() {
        return new ArrayList<>(HPacketHandler.packetPlayers.values());
    }

    /**
     * Gets values.
     *
     * @return Values.
     */
    @Nonnull
    public static Collection<HPacketPlayer> getValues() {
        return HPacketHandler.packetPlayers.values();
    }

    /**
     * Registers the player listener.
     *
     * @param player Player.
     */
    public static void register(@Nonnull Player player) {
        try {
            Objects.requireNonNull(player, "player cannot be null!");

            HPacketPlayer packetPlayer = (HPacketPlayer) HPacketHandler.packetManagerClass.getConstructor(Player.class).newInstance(player);
            HPacketHandler.packetPlayers.put(player, packetPlayer);

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
        HPacketPlayer packetPlayer = HPacketHandler.packetPlayers.remove(player);
        if (packetPlayer != null)
            packetPlayer.unregister();
    }
}