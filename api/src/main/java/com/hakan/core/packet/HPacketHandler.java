package com.hakan.core.packet;

import com.hakan.core.HCore;
import com.hakan.core.packet.player.HPacketPlayer;
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
 * HPacketHandler class.
 */
public final class HPacketHandler {

    private static final Map<Player, HPacketPlayer> packetPlayers = new HashMap<>();

    /**
     * Initializes the packet system.
     */
    public static void initialize() {
        HCore.registerEvent(PlayerJoinEvent.class)
                .priority(EventPriority.LOWEST)
                .consume(event -> HPacketHandler.register(event.getPlayer()));
        HCore.registerEvent(PlayerQuitEvent.class)
                .priority(EventPriority.LOWEST)
                .consume(event -> HPacketHandler.unregister(event.getPlayer()));
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
     * Finds packet player.
     *
     * @param player Player.
     * @return Packet player.
     */
    @Nonnull
    public static Optional<HPacketPlayer> findByPlayer(@Nonnull Player player) {
        return Optional.ofNullable(HPacketHandler.packetPlayers.get(Validate.notNull(player, "player cannot be null!")));
    }

    /**
     * Gets packet player.
     *
     * @param player Player.
     * @return Packet player.
     */
    @Nonnull
    public static HPacketPlayer getByPlayer(@Nonnull Player player) {
        return findByPlayer(player).orElseThrow(() -> new NullPointerException("there is no packet player for player: " + player));
    }

    /**
     * Registers the player listener.
     *
     * @param player Player.
     */
    public static void register(@Nonnull Player player) {
        Validate.notNull(player, "player cannot be null!");

        HPacketPlayer packetPlayer = ReflectionUtils.newInstance("com.hakan.core.packet.player.HPacketPlayer_%s",
                new Class[]{Player.class},
                new Object[]{player});
        HPacketHandler.packetPlayers.put(player, packetPlayer);
        packetPlayer.register();
    }

    /**
     * Unregisters the player listener.
     *
     * @param player Player.
     */
    public static void unregister(@Nonnull Player player) {
        Validate.notNull(player, "player cannot be null!");
        HPacketPlayer packetPlayer = HPacketHandler.packetPlayers.remove(player);
        if (packetPlayer != null)
            packetPlayer.unregister();
    }
}