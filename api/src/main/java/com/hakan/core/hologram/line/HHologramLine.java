package com.hakan.core.hologram.line;

import com.hakan.core.hologram.HHologram;
import com.hakan.core.hologram.line.entity.HHologramArmorStand;
import com.hakan.core.hologram.util.HHologramUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

/**
 * Vezor hologram line abstract class
 */
@SuppressWarnings("unused")
public final class HHologramLine {

    private final HHologram hHologram;
    private HHologramArmorStand armorStand;

    /**
     * Gets new instance of this class
     *
     * @param hHologram instance of VezorHologram
     */
    public HHologramLine(@Nonnull HHologram hHologram, @Nonnull String text) {
        this.hHologram = Objects.requireNonNull(hHologram, "hHologram cannot be null");
        this.armorStand = HHologramUtils.createVezorArmorStand(hHologram);
        this.armorStand.setText(text);
    }

    /**
     * Get instance of VezorHologram
     *
     * @return instance of VezorHologram
     */
    @Nonnull
    public HHologram getParent() {
        return this.hHologram;
    }

    /**
     * Gets text of line
     *
     * @return text of line
     */
    @Nonnull
    public String getText() {
        return this.armorStand.getText();
    }

    /**
     * Sets text of line
     *
     * @param text line text
     * @return instance of this class
     */
    @Nonnull
    public HHologramLine setText(@Nonnull String text) {
        Validate.notNull(text, "text cannot be null");
        this.armorStand.setText(text);
        return this;
    }

    /**
     * Gets VezorArmorStand class
     *
     * @param clazz your wanted class to cast
     * @return VezorArmorStand class
     */
    @Nullable
    public <T extends HHologramArmorStand> T getHArmorStand(@Nonnull Class<T> clazz) {
        Validate.notNull(clazz, "class cannot be null");
        return clazz.isInstance(this.armorStand) ? clazz.cast(this.armorStand) : null;
    }

    /**
     * Sets VezorArmorStand to hologram line
     *
     * @param hHologramArmorStand instance of VezorArmorStand class
     * @return instance of this class
     */
    @Nonnull
    public HHologramLine setVezorArmorStand(@Nonnull HHologramArmorStand hHologramArmorStand) {
        Validate.notNull(hHologramArmorStand, "vezorArmorStand cannot be null");
        this.armorStand = hHologramArmorStand;
        return this;
    }

    /**
     * Gets location of armor stand
     *
     * @return location of armor stand
     */
    @Nonnull
    public Location getLocation() {
        return this.armorStand.getLocation();
    }

    /**
     * Sets location of armor stand and update hologram
     *
     * @param location location
     * @return instance of this class
     */
    @Nonnull
    public HHologramLine setLocation(@Nonnull Location location) {
        Validate.notNull(location, "location cannot be null");
        this.armorStand.setLocation(location);
        return this;
    }

    /**
     * Shows hologram to players
     *
     * @param players player list
     * @return instance of this class
     */
    @Nonnull
    public HHologramLine show(@Nonnull List<Player> players) {
        Validate.notNull(players, "players cannot be null");
        this.armorStand.show(players);
        return this;
    }

    /**
     * Hides hologram from players
     *
     * @param players player list
     * @return instance of this class
     */
    @Nonnull
    public HHologramLine hide(@Nonnull List<Player> players) {
        Validate.notNull(players, "players cannot be null");
        this.armorStand.hide(players);
        return this;
    }
}