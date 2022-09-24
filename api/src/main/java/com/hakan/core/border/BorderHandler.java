package com.hakan.core.border;

import com.hakan.core.HCore;
import com.hakan.core.border.builder.BorderBuilder;
import com.hakan.core.border.listeners.BorderActionListeners;
import com.hakan.core.border.wrapper.Border;
import com.hakan.core.utils.Validate;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * BorderHandler class to create
 * and get world borders.
 */
public final class BorderHandler {

    private static final List<Border> borders = new ArrayList<>();

    /**
     * Initializes the world border system.
     */
    public static void initialize() {
        HCore.registerEvent(PlayerQuitEvent.class).priority(EventPriority.LOWEST)
                .filter(event -> BorderHandler.findByPlayer(event.getPlayer()).isPresent())
                .consume(event -> BorderHandler.getByPlayer(event.getPlayer()).hide(event.getPlayer()));
        HCore.registerListeners(new BorderActionListeners());
    }


    /**
     * Gets values as safe.
     *
     * @return Values.
     */
    @Nonnull
    public static Collection<Border> getValuesSafe() {
        return new ArrayList<>(BorderHandler.borders);
    }

    /**
     * Gets values.
     *
     * @return Values.
     */
    @Nonnull
    public static Collection<Border> getValues() {
        return BorderHandler.borders;
    }

    /**
     * Finds world border from player.
     *
     * @param player Player.
     * @return Border from player as optional.
     */
    @Nonnull
    public static Optional<Border> findByPlayer(@Nonnull Player player) {
        Validate.notNull(player, "player cannot be null!");
        for (Border border : BorderHandler.borders)
            if (border.getShownViewers().contains(player))
                return Optional.of(border);
        return Optional.empty();
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
     * @param location Center location.
     * @return World border builder.
     */
    @Nonnull
    public static BorderBuilder builder(@Nonnull Location location) {
        return new BorderBuilder(location);
    }
}