package com.hakan.core.border.wrapper;

import com.hakan.core.HCore;
import com.hakan.core.border.Border;
import com.hakan.core.border.BorderHandler;
import com.hakan.core.border.color.BorderColor;
import com.hakan.core.utils.Validate;
import net.minecraft.network.protocol.game.ClientboundInitializeBorderPacket;
import net.minecraft.network.protocol.game.ClientboundSetBorderCenterPacket;
import net.minecraft.network.protocol.game.ClientboundSetBorderLerpSizePacket;
import net.minecraft.network.protocol.game.ClientboundSetBorderSizePacket;
import net.minecraft.network.protocol.game.ClientboundSetBorderWarningDelayPacket;
import net.minecraft.network.protocol.game.ClientboundSetBorderWarningDistancePacket;
import net.minecraft.world.level.border.WorldBorder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

/**
 * {@inheritDoc}
 */
public final class BorderWrapper_v1_19_1_R1 extends WorldBorder implements Border {

    private BorderColor color;
    private final Set<Player> shownViewers;

    /**
     * {@inheritDoc}
     */
    public BorderWrapper_v1_19_1_R1(@Nonnull Location location, double size, double damageAmount, double damageBuffer, int warningDistance, int warningTime, @Nonnull BorderColor color) {
        this.color = Validate.notNull(color, "border color cannot be null!");
        this.shownViewers = new HashSet<>();
        super.world = ((CraftWorld) location.getWorld()).getHandle();
        this.setCenter(location);
        this.setSize(size);
        this.setDamageAmount(damageAmount);
        this.setDamageBuffer(damageBuffer);
        this.setWarningDistance(warningDistance);
        this.setWarningTime(warningTime);
        this.update();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void show(@Nonnull Player player) {
        this.shownViewers.add(Validate.notNull(player, "player cannot be null!"));

        HCore.sendPacket(player, new ClientboundSetBorderLerpSizePacket(this));
        HCore.sendPacket(player, new ClientboundSetBorderCenterPacket(this));
        HCore.sendPacket(player, new ClientboundSetBorderSizePacket(this));
        HCore.sendPacket(player, new ClientboundSetBorderWarningDistancePacket(this));
        HCore.sendPacket(player, new ClientboundSetBorderWarningDelayPacket(this));
        HCore.sendPacket(player, new ClientboundInitializeBorderPacket(this));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showAll() {
        Bukkit.getOnlinePlayers().forEach(this::show);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void hide(@Nonnull Player player) {
        this.shownViewers.remove(Validate.notNull(player, "player cannot be null!"));
        super.c(0, 0);
        super.a(Long.MAX_VALUE);
        HCore.sendPacket(player, new ClientboundSetBorderCenterPacket(this));
        HCore.sendPacket(player, new ClientboundSetBorderSizePacket(this));
        HCore.sendPacket(player, new ClientboundInitializeBorderPacket(this));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void hideAll() {
        this.shownViewers.forEach(player -> {
            super.c(0, 0);
            super.a(Long.MAX_VALUE);
            HCore.sendPacket(player, new ClientboundSetBorderCenterPacket(this));
            HCore.sendPacket(player, new ClientboundSetBorderSizePacket(this));
            HCore.sendPacket(player, new ClientboundInitializeBorderPacket(this));
        });
        this.shownViewers.clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete() {
        this.hideAll();
        BorderHandler.getValues().remove(this);
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public Set<Player> getShownViewers() {
        return this.shownViewers;
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public Location getCenter() {
        return new Location(super.world.getWorld(), super.a(), 64, super.b());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCenter(@Nonnull Location location) {
        Validate.notNull(location, "location cannot be null!");
        super.c(location.getX(), location.getZ());
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
    public double getSize() {
        return super.i();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSize(double size) {
        super.a(size);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSize(double size, long time) {
        super.a(this.getSize(), size, time);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getDamageAmount() {
        return super.o();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDamageAmount(double damageAmount) {
        super.c(damageAmount);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getDamageBuffer() {
        return super.n();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDamageBuffer(double damageBuffer) {
        super.b(damageBuffer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getWarningDistance() {
        return super.r();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setWarningDistance(int warningDistance) {
        super.c(warningDistance);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getWarningTime() {
        return super.q();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setWarningTime(int warningTime) {
        super.b(warningTime);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update() {
        if (this.color == BorderColor.BLUE)
            super.a(super.i(), super.i(), Long.MAX_VALUE);
        else if (this.color == BorderColor.GREEN)
            super.a(super.i(), super.i() + 0.1, Long.MAX_VALUE);
        else if (this.color == BorderColor.RED)
            super.a(super.i(), super.i() - 0.1, Long.MAX_VALUE);
        this.shownViewers.forEach(this::show);
    }
}