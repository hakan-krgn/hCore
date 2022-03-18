package com.hakan.core.worldborder.border;

import com.hakan.core.HCore;
import com.hakan.core.worldborder.HWorldBorderHandler;
import net.minecraft.server.v1_12_R1.PacketPlayOutWorldBorder;
import net.minecraft.server.v1_12_R1.WorldBorder;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * {@inheritDoc}
 */
public final class HWorldBorder_v1_12_R1 extends WorldBorder implements HWorldBorder {

    private HBorderColor color;
    private final Set<Player> shownViewers;

    /**
     * {@inheritDoc}
     */
    public HWorldBorder_v1_12_R1(@Nonnull Location location, double size, double damageAmount, double damageBuffer, int warningDistance, int warningTime, @Nonnull HBorderColor color) {
        this.color = Objects.requireNonNull(color, "border color cannot be null!");
        this.shownViewers = new HashSet<>();
        super.world = ((CraftWorld) location.getWorld()).getHandle();
        super.setCenter(location.getX(), location.getZ());
        super.setSize(size);
        super.setDamageAmount(damageAmount);
        super.setDamageBuffer(damageBuffer);
        super.setWarningDistance(warningDistance);
        super.setWarningTime(warningTime);
        this.update();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void show(@Nonnull Player player) {
        this.shownViewers.add(Objects.requireNonNull(player, "player cannot be null!"));
        HCore.sendPacket(player, new PacketPlayOutWorldBorder(this, PacketPlayOutWorldBorder.EnumWorldBorderAction.INITIALIZE));
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
        this.shownViewers.remove(Objects.requireNonNull(player, "player cannot be null!"));
        super.setCenter(0, 0);
        super.setSize(59999998);
        HCore.sendPacket(player, new PacketPlayOutWorldBorder(this, PacketPlayOutWorldBorder.EnumWorldBorderAction.INITIALIZE));
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
        HWorldBorderHandler.getValues().remove(this);
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
        return new Location(super.world.getWorld(), super.getCenterX(), 64, super.getCenterZ());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCenter(@Nonnull Location location) {
        Validate.notNull(location, "location cannot be null!");
        super.setCenter(location.getX(), location.getZ());
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public HBorderColor getColor() {
        return this.color;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setColor(@Nonnull HBorderColor hBorderColor) {
        this.color = Objects.requireNonNull(hBorderColor, "border color cannot be null!");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update() {
        if (this.color == HBorderColor.BLUE)
            super.transitionSizeBetween(super.getSize(), super.getSize(), Long.MAX_VALUE);
        else if (this.color == HBorderColor.GREEN)
            super.transitionSizeBetween(super.getSize(), super.getSize() + 0.1, Long.MAX_VALUE);
        else if (this.color == HBorderColor.RED)
            super.transitionSizeBetween(super.getSize(), super.getSize() - 0.1, Long.MAX_VALUE);
        this.shownViewers.forEach(this::show);
    }
}