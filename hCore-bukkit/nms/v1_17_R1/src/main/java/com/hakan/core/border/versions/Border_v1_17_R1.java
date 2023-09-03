package com.hakan.core.border.versions;

import com.hakan.core.HCore;
import com.hakan.core.border.Border;
import com.hakan.core.border.BorderHandler;
import com.hakan.core.border.color.BorderColor;
import com.hakan.core.scheduler.Scheduler;
import com.hakan.core.utils.Validate;
import net.minecraft.network.protocol.game.ClientboundInitializeBorderPacket;
import net.minecraft.world.level.border.WorldBorder;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.concurrent.TimeUnit;

/**
 * {@inheritDoc}
 */
public final class Border_v1_17_R1 implements Border {

    private final Player viewer;
    private final WorldBorder border;
    private final Scheduler scheduler;

    private BorderColor color;
    private boolean transition;

    /**
     * {@inheritDoc}
     */
    private Border_v1_17_R1(@Nonnull Player viewer,
                            @Nonnull Location location,
                            @Nonnull BorderColor color,
                            double size,
                            double damageAmount,
                            double damageBuffer,
                            int warningDistance,
                            int warningTime) {
        this.scheduler = HCore.syncScheduler();
        this.viewer = Validate.notNull(viewer, "viewer cannot be null!");
        this.color = Validate.notNull(color, "border color cannot be null!");

        this.border = new WorldBorder();
        this.border.world = ((CraftWorld) location.getWorld()).getHandle();

        this.setSize(size);
        this.setCenter(location);
        this.setWarningTime(warningTime);
        this.setDamageAmount(damageAmount);
        this.setDamageBuffer(damageBuffer);
        this.setWarningDistance(warningDistance);
        this.update();
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public Player getViewer() {
        return this.viewer;
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public Location getCenter() {
        return new Location(this.border.world.getWorld(), this.border.getCenterX(), 64, this.border.getCenterZ());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCenter(@Nonnull Location location) {
        Validate.notNull(location, "location cannot be null!");
        this.border.setCenter(location.getX(), location.getZ());
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public BorderColor getColor() {
        return this.color;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setColor(@Nonnull BorderColor borderColor) {
        this.color = Validate.notNull(borderColor, "border color cannot be null!");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getDamageAmount() {
        return this.border.getDamageAmount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDamageAmount(double damageAmount) {
        this.border.setDamageAmount(damageAmount);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getDamageBuffer() {
        return this.border.getDamageBuffer();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDamageBuffer(double damageBuffer) {
        this.border.setDamageBuffer(damageBuffer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getWarningDistance() {
        return this.border.getWarningDistance();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setWarningDistance(int warningDistance) {
        this.border.setWarningDistance(warningDistance);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getWarningTime() {
        return this.border.getWarningTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setWarningTime(int warningTime) {
        this.border.setWarningTime(warningTime);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getSize() {
        return this.border.getSize();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSize(double size, long time) {
        if (this.transition)
            this.scheduler.cancel();

        this.scheduler.after(time, TimeUnit.MILLISECONDS)
                .run(() -> this.transition = false);
        this.transition = true;

        this.border.transitionSizeBetween(this.getSize(), size, time);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update() {
        if (!this.transition)
            if (this.color == BorderColor.BLUE)
                this.border.transitionSizeBetween(this.getSize(), this.getSize(), Long.MAX_VALUE);
            else if (this.color == BorderColor.GREEN)
                this.border.transitionSizeBetween(this.getSize(), this.getSize() + 0.1, Long.MAX_VALUE);
            else if (this.color == BorderColor.RED)
                this.border.transitionSizeBetween(this.getSize(), this.getSize() - 0.1, Long.MAX_VALUE);

        HCore.sendPacket(this.viewer, new ClientboundInitializeBorderPacket(this.border));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete() {
        this.setSize(6.0E7);
        this.setWarningTime(15);
        this.setDamageAmount(0.2);
        this.setDamageBuffer(5.0);
        this.setWarningDistance(5);
        this.setCenter(new Location(this.border.world.getWorld(), 0, 64, 0));

        HCore.sendPacket(this.viewer, new ClientboundInitializeBorderPacket(this.border));
        BorderHandler.getContent().remove(this.viewer);
    }
}
