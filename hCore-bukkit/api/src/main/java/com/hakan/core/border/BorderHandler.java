package com.hakan.core.border;

import com.hakan.core.HCore;
import com.hakan.core.border.builder.BorderBuilder;
import com.hakan.core.utils.Validate;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * BorderHandler class to create
 * and get world borders.
 */
public final class BorderHandler {

    private static final Map<Player, Border> borders = new HashMap<>();

    /**
     * Initializes the world border system.
     */
    public static void initialize() {
        HCore.registerEvent(PlayerQuitEvent.class)
                .consume(event -> BorderHandler.findByPlayer(event.getPlayer()).ifPresent(Border::delete));

        HCore.registerEvent(PlayerChangedWorldEvent.class)
                .consume(event -> BorderHandler.findByPlayer(event.getPlayer()).ifPresent(Border::delete));

        HCore.registerEvent(PlayerTeleportEvent.class)
                .filter(event -> event.getFrom().getWorld() != null)
                .filter(event -> event.getTo() != null && event.getTo().getWorld() != null)
                .filter(event -> Objects.equals(event.getTo().getWorld(), event.getFrom().getWorld()))
                .consume(event -> BorderHandler.findByPlayer(event.getPlayer()).ifPresent(Border::delete));
    }


    /**
     * Gets content as safe.
     *
     * @return Content.
     */
    @Nonnull
    public static Map<Player, Border> getContentSafe() {
        return new HashMap<>(borders);
    }

    /**
     * Gets content.
     *
     * @return Content.
     */
    @Nonnull
    public static Map<Player, Border> getContent() {
        return borders;
    }

    /**
     * Gets values as safe.
     *
     * @return Values.
     */
    @Nonnull
    public static Collection<Border> getValuesSafe() {
        return new ArrayList<>(borders.values());
    }

    /**
     * Gets values.
     *
     * @return Values.
     */
    @Nonnull
    public static Collection<Border> getValues() {
        return borders.values();
    }

    /**
     * Finds world border from player.
     *
     * @param player Player.
     * @return Border from player as optional.
     */
    @Nonnull
    public static Optional<Border> findByPlayer(@Nonnull Player player) {
        return Optional.ofNullable(borders.get(Validate.notNull(player, "player cannot be null!")));
    }

    /**
     * Gets world border from player.
     *
     * @param player Player.
     * @return Border from player.
     */
    @Nonnull
    public static Border getByPlayer(@Nonnull Player player) {
        return BorderHandler.findByPlayer(player).orElseThrow(() -> new NullPointerException("this player doesn't have a inventory!"));
    }

    /**
     * Creates world border builder.
     *
     * @param viewer Border viewer.
     * @return World border builder.
     */
    @Nonnull
    public static BorderBuilder builder(@Nonnull Player viewer) {
        return new BorderBuilder(viewer);
    }
}
