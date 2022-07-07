package com.hakan.core.hologram.line;

import com.hakan.core.hologram.HHologram;
import com.hakan.core.hologram.line.entity.HHologramArmorStand;
import com.hakan.core.hologram.util.HHologramUtils;
import com.hakan.core.utils.Validate;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Hologram line abstract class.
 */
@SuppressWarnings("unused")
public final class HHologramLine {

    private final HHologram hHologram;
    private final HHologramArmorStand armorStand;

    /**
     * Gets new instance of this class.
     *
     * @param hHologram Hologram.
     * @param text      Text of line.
     */
    public HHologramLine(@Nonnull HHologram hHologram, @Nonnull String text) {
        this.hHologram = Validate.notNull(hHologram, "hHologram cannot be null!");
        this.armorStand = HHologramUtils.createHologramArmorStand(hHologram);
        this.armorStand.setText(Validate.notNull(text, "text cannot be null!"));
    }

    /**
     * Get instance of HHologram.
     *
     * @return instance of HHologram.
     */
    @Nonnull
    public HHologram getParent() {
        return this.hHologram;
    }

    /**
     * Gets text of line.
     *
     * @return Text from line.
     */
    @Nonnull
    public String getText() {
        return this.armorStand.getText();
    }

    /**
     * Sets text of line.
     *
     * @param text Line text.
     * @return Instance of this class.
     */
    @Nonnull
    public HHologramLine setText(@Nonnull String text) {
        this.armorStand.setText(Validate.notNull(text, "text cannot be null!"));
        return this;
    }

    /**
     * Gets location of armor stand.
     *
     * @return Location of armor stand.
     */
    @Nonnull
    public Location getLocation() {
        return this.armorStand.getLocation();
    }

    /**
     * Sets location of armor stand and update hologram.
     *
     * @param location Location.
     * @return Instance of this class.
     */
    @Nonnull
    public HHologramLine setLocation(@Nonnull Location location) {
        this.armorStand.setLocation(Validate.notNull(location, "location cannot be null!"));
        return this;
    }

    /**
     * Shows hologram to players.
     *
     * @param players Player list.
     * @return Instance of this class.
     */
    @Nonnull
    public HHologramLine show(@Nonnull List<Player> players) {
        this.armorStand.show(Validate.notNull(players, "players cannot be null!"));
        return this;
    }

    /**
     * Hides hologram from players.
     *
     * @param players Player list.
     * @return Instance of this class.
     */
    @Nonnull
    public HHologramLine hide(@Nonnull List<Player> players) {
        this.armorStand.hide(Validate.notNull(players, "players cannot be null!"));
        return this;
    }
}