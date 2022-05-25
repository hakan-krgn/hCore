package com.hakan.core.worldborder;

import com.hakan.core.HCore;
import com.hakan.core.worldborder.border.HBorderColor;
import com.hakan.core.worldborder.border.HWorldBorder;
import com.hakan.core.worldborder.listeners.HBorderPlayerActionListener;
import com.hakan.core.worldborder.listeners.HBorderPlayerConnectionListener;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * HWorldBorderHandler class to create
 * and get world borders.
 */
public final class HWorldBorderHandler {

    private static final List<HWorldBorder> borders = new ArrayList<>();

    /**
     * Initializes the world border system.
     */
    public static void initialize() {
        HCore.registerListeners(new HBorderPlayerActionListener(),
                new HBorderPlayerConnectionListener());
    }


    /**
     * Gets values as safe.
     *
     * @return Values.
     */
    @Nonnull
    public static Collection<HWorldBorder> getValuesSafe() {
        return new ArrayList<>(HWorldBorderHandler.borders);
    }

    /**
     * Gets values.
     *
     * @return Values.
     */
    @Nonnull
    public static Collection<HWorldBorder> getValues() {
        return HWorldBorderHandler.borders;
    }

    /**
     * Finds world border from player.
     *
     * @param player Player.
     * @return HWorldBorder from player as optional.
     */
    @Nonnull
    public static Optional<HWorldBorder> findByPlayer(@Nonnull Player player) {
        Objects.requireNonNull(player, "player cannot be null!");
        for (HWorldBorder hWorldBorder : HWorldBorderHandler.borders)
            if (hWorldBorder.getShownViewers().contains(player))
                return Optional.of(hWorldBorder);
        return Optional.empty();
    }

    /**
     * Gets world border from player.
     *
     * @param player Player.
     * @return HWorldBorder from player.
     */
    @Nonnull
    public static HWorldBorder getByPlayer(@Nonnull Player player) {
        return HWorldBorderHandler.findByPlayer(player).orElseThrow(() -> new NullPointerException("this player doesn't have a inventory!"));
    }

    /**
     * Creates world border.
     *
     * @param location        Center location.
     * @param size            Size of world border.
     * @param damageAmount    Damage amount.
     * @param damageBuffer    Damage buffer.
     * @param warningDistance Warning distance.
     * @param warningTime     Warning time.
     * @param color           Color of border.
     * @return Created world border.
     */
    @Nonnull
    public static HWorldBorder create(@Nonnull Location location, double size, double damageAmount, double damageBuffer, int warningDistance, int warningTime, @Nonnull HBorderColor color) {
        Objects.requireNonNull(location, "location cannot be null!");
        Objects.requireNonNull(color, "border color cannot be null!");

        try {
            HWorldBorder hWorldBorder = (HWorldBorder) Class.forName("com.hakan.core.worldborder.border.HWorldBorder_" + HCore.getVersionString())
                    .getConstructor(Location.class, double.class, double.class, double.class, int.class, int.class, HBorderColor.class)
                    .newInstance(location, size, damageAmount, damageBuffer, warningDistance, warningTime, color);
            HWorldBorderHandler.borders.add(hWorldBorder);
            return hWorldBorder;
        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        }
    }
}