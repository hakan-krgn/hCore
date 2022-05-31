package com.hakan.core.packet;

import com.hakan.core.HCore;
import com.hakan.core.packet.player.HPacketPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
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
        return Optional.ofNullable(HPacketHandler.packetPlayers.get(Objects.requireNonNull(player, "player cannot be null!")));
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
        Objects.requireNonNull(player, "player cannot be null!");

        try {
            HPacketPlayer packetPlayer = (HPacketPlayer) Class.forName("com.hakan.core.packet.player.HPacketPlayer_" + HCore.getVersionString())
                    .getConstructor(Player.class).newInstance(player);
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