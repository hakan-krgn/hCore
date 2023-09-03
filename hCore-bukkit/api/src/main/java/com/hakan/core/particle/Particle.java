package com.hakan.core.particle;

import com.hakan.core.particle.type.ParticleType;
import com.hakan.core.utils.Validate;
import org.bukkit.Color;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;

/**
 * Particle class.
 */
public final class Particle {

    private String name;
    private Vector offset;
    private Color color;
    private int amount;
    private double speed;

    /**
     * Creates new instance of this class.
     *
     * @param name   Particle name.
     * @param amount Amount.
     * @param speed  Speed.
     * @param offset Offset.
     * @param color  Particle Color.
     */
    public Particle(@Nonnull String name, int amount, double speed, @Nonnull Vector offset, @Nonnull Color color) {
        this.name = Validate.notNull(name, "particle name cannot be null!");
        this.offset = Validate.notNull(offset, "offset cannot be null!");
        this.color = Validate.notNull(color, "color cannot be null!");
        this.amount = amount;
        this.speed = speed;
    }

    /**
     * Creates new instance of this class.
     *
     * @param name   Particle name.
     * @param amount Amount.
     * @param speed  Speed.
     * @param offset Offset.
     */
    public Particle(@Nonnull String name, int amount, double speed, @Nonnull Vector offset) {
        this(name, amount, speed, offset, Color.WHITE);
    }

    /**
     * Creates new instance of this class.
     *
     * @param name   Particle name.
     * @param amount Amount.
     * @param offset Offset.
     */
    public Particle(@Nonnull String name, int amount, @Nonnull Vector offset) {
        this(name, amount, 0.1, offset, Color.WHITE);
    }

    /**
     * Creates new instance of this class.
     *
     * @param name   Particle name.
     * @param speed  Amount.
     * @param offset Offset.
     */
    public Particle(@Nonnull String name, double speed, @Nonnull Vector offset) {
        this(name, 10, speed, offset, Color.WHITE);
    }

    /**
     * Creates new instance of this class.
     *
     * @param type   Particle type.
     * @param amount Amount.
     * @param speed  Speed.
     * @param offset Offset.
     * @param color  Particle Color.
     */
    public Particle(@Nonnull ParticleType type, int amount, double speed, @Nonnull Vector offset, @Nonnull Color color) {
        this(type.name(), amount, speed, offset, color);
    }

    /**
     * Creates new instance of this class.
     *
     * @param type   Particle type.
     * @param amount Amount.
     * @param speed  Speed.
     * @param offset Offset.
     */
    public Particle(@Nonnull ParticleType type, int amount, double speed, @Nonnull Vector offset) {
        this(type.name(), amount, speed, offset, Color.WHITE);
    }

    /**
     * Creates new instance of this class.
     *
     * @param type   Particle type.
     * @param amount Amount.
     * @param offset Offset.
     */
    public Particle(@Nonnull ParticleType type, int amount, @Nonnull Vector offset) {
        this(type.name(), amount, 0.1, offset, Color.WHITE);
    }

    /**
     * Creates new instance of this class.
     *
     * @param type   Particle type.
     * @param speed  Amount.
     * @param offset Offset.
     */
    public Particle(@Nonnull ParticleType type, double speed, @Nonnull Vector offset) {
        this(type.name(), 10, speed, offset, Color.WHITE);
    }

    /**
     * Gets particle name.
     *
     * @return Particle name.
     */
    @Nonnull
    public String getName() {
        return this.name;
    }

    /**
     * Sets particle name.
     *
     * @param particleName Particle name.
     */
    public void setName(@Nonnull String particleName) {
        this.name = Validate.notNull(particleName, "particle name cannot be null!");
    }

    /**
     * Gets offset.
     *
     * @return Offset.
     */
    @Nonnull
    public Vector getOffset() {
        return this.offset;
    }

    /**
     * Sets offset.
     *
     * @param offset Offset.
     */
    public void setOffset(@Nonnull Vector offset) {
        this.offset = Validate.notNull(offset, "offset cannot be null!");
    }

    /**
     * Gets color.
     *
     * @return Color.
     */
    @Nonnull
    public Color getColor() {
        return this.color;
    }

    /**
     * Sets color.
     *
     * @param color Color.
     */
    public void setColor(@Nonnull Color color) {
        this.color = Validate.notNull(color, "color cannot be null!");
    }

    /**
     * Gets amount.
     *
     * @return Amount.
     */
    public int getAmount() {
        return this.amount;
    }

    /**
     * Gets amount.
     *
     * @param amount Amount.
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }

    /**
     * Gets speed.
     *
     * @return Speed.
     */
    public double getSpeed() {
        return this.speed;
    }

    /**
     * Sets speed.
     *
     * @param speed Speed.
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }
}
