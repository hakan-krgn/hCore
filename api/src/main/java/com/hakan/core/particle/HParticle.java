package com.hakan.core.particle;

import com.hakan.core.particle.type.HParticleType;
import com.hakan.core.utils.Validate;
import org.bukkit.Color;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;

/**
 * HParticle class.
 */
public final class HParticle {

    private String particleName;
    private Vector offset;
    private Color color;
    private int amount;
    private double speed;

    /**
     * Creates new instance of this class.
     *
     * @param particleName Particle name.
     * @param amount       Amount.
     * @param speed        Speed.
     * @param offset       Offset.
     * @param color        Particle Color.
     */
    public HParticle(@Nonnull String particleName, int amount, double speed, @Nonnull Vector offset, @Nonnull Color color) {
        this.particleName = Validate.notNull(particleName, "particle name cannot be null!");
        this.offset = Validate.notNull(offset, "offset cannot be null!");
        this.color = Validate.notNull(color, "color cannot be null!");
        this.amount = amount;
        this.speed = speed;
    }

    /**
     * Creates new instance of this class.
     *
     * @param particleName Particle name.
     * @param amount       Amount.
     * @param speed        Speed.
     * @param offset       Offset.
     */
    public HParticle(@Nonnull String particleName, int amount, double speed, @Nonnull Vector offset) {
        this(particleName, amount, speed, offset, Color.WHITE);
    }

    /**
     * Creates new instance of this class.
     *
     * @param particleName Particle name.
     * @param amount       Amount.
     * @param offset       Offset.
     */
    public HParticle(@Nonnull String particleName, int amount, @Nonnull Vector offset) {
        this(particleName, amount, 0.1, offset, Color.WHITE);
    }

    /**
     * Creates new instance of this class.
     *
     * @param particleName Particle name.
     * @param speed        Amount.
     * @param offset       Offset.
     */
    public HParticle(@Nonnull String particleName, double speed, @Nonnull Vector offset) {
        this(particleName, 10, speed, offset, Color.WHITE);
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
    public HParticle(@Nonnull HParticleType type, int amount, double speed, @Nonnull Vector offset, @Nonnull Color color) {
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
    public HParticle(@Nonnull HParticleType type, int amount, double speed, @Nonnull Vector offset) {
        this(type.name(), amount, speed, offset, Color.WHITE);
    }

    /**
     * Creates new instance of this class.
     *
     * @param type   Particle type.
     * @param amount Amount.
     * @param offset Offset.
     */
    public HParticle(@Nonnull HParticleType type, int amount, @Nonnull Vector offset) {
        this(type.name(), amount, 0.1, offset, Color.WHITE);
    }

    /**
     * Creates new instance of this class.
     *
     * @param type   Particle type.
     * @param speed  Amount.
     * @param offset Offset.
     */
    public HParticle(@Nonnull HParticleType type, double speed, @Nonnull Vector offset) {
        this(type.name(), 10, speed, offset, Color.WHITE);
    }

    /**
     * Gets particle name.
     *
     * @return Particle name.
     */
    @Nonnull
    public String getParticleName() {
        return this.particleName;
    }

    /**
     * Sets particle name.
     *
     * @param particleName Particle name.
     */
    public void setParticleName(@Nonnull String particleName) {
        this.particleName = Validate.notNull(particleName, "particle name cannot be null!");
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