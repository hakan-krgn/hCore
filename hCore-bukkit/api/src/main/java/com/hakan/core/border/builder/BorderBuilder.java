package com.hakan.core.border.builder;

import com.hakan.core.border.Border;
import com.hakan.core.border.BorderHandler;
import com.hakan.core.border.color.BorderColor;
import com.hakan.core.utils.ReflectionUtils;
import com.hakan.core.utils.Validate;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * BorderBuilder class to create
 * world borders easily.
 */
public final class BorderBuilder {

    private final Player viewer;
    private Location center;
    private BorderColor color;
    private double size;
    private double damageAmount;
    private double damageBuffer;
    private int warningTime;
    private int warningDistance;

    /**
     * Creates a new BorderBuilder instance.
     *
     * @param viewer Border viewer.
     */
    public BorderBuilder(@Nonnull Player viewer) {
        this.viewer = Validate.notNull(viewer, "viewer cannot be null!");
        this.center = new Location(viewer.getWorld(), 0, 0, 0);
        this.color = BorderColor.BLUE;
        this.size = 16;
        this.damageAmount = 0;
        this.damageBuffer = 0;
        this.warningTime = 0;
        this.warningDistance = 0;
    }

    /**
     * Sets center location.
     *
     * @param location Center location.
     * @return BorderBuilder instance.
     */
    @Nonnull
    public BorderBuilder location(@Nonnull Location location) {
        this.center = Validate.notNull(location, "location cannot be null!");
        return this;
    }

    /**
     * Sets border color.
     *
     * @param color Border color.
     * @return BorderBuilder instance.
     */
    @Nonnull
    public BorderBuilder color(@Nonnull BorderColor color) {
        this.color = Validate.notNull(color, "border color cannot be null!");
        return this;
    }

    /**
     * Sets border size.
     *
     * @param size Border size.
     * @return BorderBuilder instance.
     */
    @Nonnull
    public BorderBuilder size(double size) {
        this.size = size;
        return this;
    }

    /**
     * Sets damage amount.
     *
     * @param damageAmount Damage amount.
     * @return BorderBuilder instance.
     */
    @Nonnull
    public BorderBuilder damageAmount(double damageAmount) {
        this.damageAmount = damageAmount;
        return this;
    }

    /**
     * Sets damage buffer.
     *
     * @param damageBuffer Damage buffer.
     * @return BorderBuilder instance.
     */
    @Nonnull
    public BorderBuilder damageBuffer(double damageBuffer) {
        this.damageBuffer = damageBuffer;
        return this;
    }

    /**
     * Sets warning time.
     *
     * @param warningTime Warning time.
     * @return BorderBuilder instance.
     */
    @Nonnull
    public BorderBuilder warningTime(int warningTime) {
        this.warningTime = warningTime;
        return this;
    }

    /**
     * Sets warning distance.
     *
     * @param warningDistance Warning distance.
     * @return BorderBuilder instance.
     */
    @Nonnull
    public BorderBuilder warningDistance(int warningDistance) {
        this.warningDistance = warningDistance;
        return this;
    }

    /**
     * Builds world border.
     *
     * @return Created world border.
     */
    @Nonnull
    public Border build() {
        Border border = ReflectionUtils.newInstance("com.hakan.core.border.versions.Border_%s",
                new Class[]{Player.class, Location.class, BorderColor.class, double.class, double.class, double.class, int.class, int.class},
                new Object[]{this.viewer, this.center, this.color, this.size, this.damageAmount, this.damageBuffer, this.warningDistance, this.warningTime});

        BorderHandler.getContent().put(this.viewer, border);
        return border;
    }
}
