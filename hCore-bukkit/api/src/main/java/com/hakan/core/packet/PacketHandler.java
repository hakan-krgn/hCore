package com.hakan.core.packet;

import com.hakan.core.HCore;
import com.hakan.core.packet.player.PacketPlayer;
import com.hakan.core.utils.ReflectionUtils;
import com.hakan.core.utils.Validate;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * PacketHandler class.
 */
public final class PacketHandler {

    private static final Map<Player, PacketPlayer> packetPlayers = new HashMap<>();

    /**
     * Initializes the packet system.
     */
    public static void initialize() {
        HCore.registerEvent(PlayerJoinEvent.class)
                .priority(EventPriority.LOWEST)
                .consume(event -> PacketHandler.register(event.getPlayer()));
        HCore.registerEvent(PlayerQuitEvent.class)
                .priority(EventPriority.LOWEST)
                .consume(event -> PacketHandler.unregister(event.getPlayer()));
    }


    /**
     * Gets content as safe.
     *
     * @return Content.
     */
    @Nonnull
    public static Map<Player, PacketPlayer> getContentSafe() {
        return new HashMap<>(packetPlayers);
    }

    /**
     * Gets content.
     *
     * @return Content.
     */
    @Nonnull
    public static Map<Player, PacketPlayer> getContent() {
        return packetPlayers;
    }

    /**
     * Gets values as safe.
     *
     * @return Values.
     */
    @Nonnull
    public static Collection<PacketPlayer> getValuesSafe() {
        return new ArrayList<>(packetPlayers.values());
    }

    /**
     * Gets values.
     *
     * @return Values.
     */
    @Nonnull
    public static Collection<PacketPlayer> getValues() {
        return packetPlayers.values();
    }

    /**
     * Finds packet player.
     *
     * @param player Player.
     * @return Packet player.
     */
    @Nonnull
    public static Optional<PacketPlayer> findByPlayer(@Nonnull Player player) {
        return Optional.ofNullable(packetPlayers.get(Validate.notNull(player, "player cannot be null!")));
    }

    /**
     * Gets packet player.
     *
     * @param player Player.
     * @return Packet player.
     */
    @Nonnull
    public static PacketPlayer getByPlayer(@Nonnull Player player) {
        return findByPlayer(player).orElseThrow(() -> new NullPointerException("there is no packet player for player: " + player));
    }

    /**
     * Registers the player listener.
     *
     * @param player Player.
     */
    public static void register(@Nonnull Player player) {
        Validate.notNull(player, "player cannot be null!");

        PacketPlayer packetPlayer = ReflectionUtils.newInstance("com.hakan.core.packet.player.PacketPlayer_%s",
                new Class[]{Player.class}, new Object[]{player});
        packetPlayers.put(player, packetPlayer);
        packetPlayer.register();
    }

    /**
     * Unregisters the player listener.
     *
     * @param player Player.
     */
    public static void unregister(@Nonnull Player player) {
        Validate.notNull(player, "player cannot be null!");
        PacketPlayer packetPlayer = packetPlayers.remove(player);
        if (packetPlayer != null) packetPlayer.unregister();
    }
}
