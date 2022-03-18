package com.hakan.core.worldborder.border;

import com.hakan.core.HCore;
import com.hakan.core.worldborder.HWorldBorderHandler;
import net.minecraft.network.protocol.game.*;
import net.minecraft.world.level.border.WorldBorder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R1.CraftWorld;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

/**
 * {@inheritDoc}
 */
public final class HWorldBorder_v1_18_R1 extends WorldBorder implements HWorldBorder {

    private HBorderColor color;
    private final Set<Player> shownViewers;

    /**
     * {@inheritDoc}
     */
    public HWorldBorder_v1_18_R1(Location location, double size, double damageAmount, double damageBuffer, int warningDistance, int warningTime, HBorderColor color) {
        this.color = color;
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
    public void show(Player player) {
        this.shownViewers.add(player);

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
    public void hide(Player player) {
        this.shownViewers.remove(player);
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
        this.shownViewers.forEach(this::hide);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete() {
        this.hideAll();
        HWorldBorderHandler.getContent().remove(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Player> getShownViewers() {
        return this.shownViewers;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Location getCenter() {
        return new Location(super.world.getWorld(), super.a(), 64, super.b());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCenter(Location location) {
        super.c(location.getX(), location.getZ());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HBorderColor getColor() {
        return this.color;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setColor(HBorderColor hBorderColor) {
        this.color = hBorderColor;
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
        if (this.color == HBorderColor.BLUE)
            super.a(super.i(), super.i(), Long.MAX_VALUE);
        else if (this.color == HBorderColor.GREEN)
            super.a(super.i(), super.i() + 0.1, Long.MAX_VALUE);
        else if (this.color == HBorderColor.RED)
            super.a(super.i(), super.i() - 0.1, Long.MAX_VALUE);
        this.shownViewers.forEach(this::show);
    }
}