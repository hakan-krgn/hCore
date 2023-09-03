package com.hakan.core.border.versions;

import com.hakan.core.HCore;
import com.hakan.core.border.Border;
import com.hakan.core.border.BorderHandler;
import com.hakan.core.border.color.BorderColor;
import com.hakan.core.scheduler.Scheduler;
import com.hakan.core.utils.Validate;
import net.minecraft.network.protocol.game.ClientboundInitializeBorderPacket;
import net.minecraft.network.protocol.game.ClientboundSetBorderCenterPacket;
import net.minecraft.network.protocol.game.ClientboundSetBorderLerpSizePacket;
import net.minecraft.network.protocol.game.ClientboundSetBorderSizePacket;
import net.minecraft.network.protocol.game.ClientboundSetBorderWarningDelayPacket;
import net.minecraft.network.protocol.game.ClientboundSetBorderWarningDistancePacket;
import net.minecraft.world.level.border.WorldBorder;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.concurrent.TimeUnit;

/**
 * {@inheritDoc}
 */
public final class Border_v1_19_1_R1 implements Border {

    private final Player viewer;
    private final WorldBorder border;
    private final Scheduler scheduler;

    private BorderColor color;
    private boolean transition;

    /**
     * {@inheritDoc}
     */
    private Border_v1_19_1_R1(@Nonnull Player viewer,
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
        return new Location(this.border.world.getWorld(), this.border.a(), 64, this.border.b());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCenter(@Nonnull Location location) {
        Validate.notNull(location, "location cannot be null!");
        this.border.c(location.getX(), location.getZ());
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
        return this.border.o();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDamageAmount(double damageAmount) {
        this.border.c(damageAmount);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getDamageBuffer() {
        return this.border.n();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDamageBuffer(double damageBuffer) {
        this.border.b(damageBuffer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getWarningDistance() {
        return this.border.r();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setWarningDistance(int warningDistance) {
        this.border.c(warningDistance);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getWarningTime() {
        return this.border.q();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setWarningTime(int warningTime) {
        this.border.b(warningTime);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getSize() {
        return this.border.i();
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

        this.border.a(this.getSize(), size, time);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update() {
        if (!this.transition)
            if (this.color == BorderColor.BLUE)
                this.border.a(this.getSize(), this.getSize(), Long.MAX_VALUE);
            else if (this.color == BorderColor.GREEN)
                this.border.a(this.getSize(), this.getSize() + 0.1, Long.MAX_VALUE);
            else if (this.color == BorderColor.RED)
                this.border.a(this.getSize(), this.getSize() - 0.1, Long.MAX_VALUE);

        HCore.sendPacket(this.viewer, new ClientboundSetBorderLerpSizePacket(this.border));
        HCore.sendPacket(this.viewer, new ClientboundSetBorderCenterPacket(this.border));
        HCore.sendPacket(this.viewer, new ClientboundSetBorderSizePacket(this.border));
        HCore.sendPacket(this.viewer, new ClientboundSetBorderWarningDistancePacket(this.border));
        HCore.sendPacket(this.viewer, new ClientboundSetBorderWarningDelayPacket(this.border));
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

        HCore.sendPacket(this.viewer, new ClientboundSetBorderLerpSizePacket(this.border));
        HCore.sendPacket(this.viewer, new ClientboundSetBorderCenterPacket(this.border));
        HCore.sendPacket(this.viewer, new ClientboundSetBorderSizePacket(this.border));
        HCore.sendPacket(this.viewer, new ClientboundSetBorderWarningDistancePacket(this.border));
        HCore.sendPacket(this.viewer, new ClientboundSetBorderWarningDelayPacket(this.border));
        HCore.sendPacket(this.viewer, new ClientboundInitializeBorderPacket(this.border));
        BorderHandler.getContent().remove(this.viewer);
    }
}
