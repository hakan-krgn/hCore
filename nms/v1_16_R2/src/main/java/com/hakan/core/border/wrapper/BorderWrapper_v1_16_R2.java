package com.hakan.core.border.wrapper;

import com.hakan.core.HCore;
import com.hakan.core.border.Border;
import com.hakan.core.border.BorderHandler;
import com.hakan.core.border.color.BorderColor;
import com.hakan.core.utils.Validate;
import net.minecraft.server.v1_16_R2.PacketPlayOutWorldBorder;
import net.minecraft.server.v1_16_R2.WorldBorder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R2.CraftWorld;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

/**
 * {@inheritDoc}
 */
public final class BorderWrapper_v1_16_R2 extends WorldBorder implements Border {

    private BorderColor color;
    private final Set<Player> shownViewers;

    /**
     * {@inheritDoc}
     */
    public BorderWrapper_v1_16_R2(@Nonnull Location location, double size, double damageAmount, double damageBuffer, int warningDistance, int warningTime, @Nonnull BorderColor color) {
        this.color = Validate.notNull(color, "border color cannot be null!");
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
        this.shownViewers.add(Validate.notNull(player, "player cannot be null!"));
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
        this.shownViewers.remove(Validate.notNull(player, "player cannot be null!"));
        super.setCenter(0, 0);
        super.setSize(59999998);
        HCore.sendPacket(player, new PacketPlayOutWorldBorder(this, PacketPlayOutWorldBorder.EnumWorldBorderAction.INITIALIZE));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void hideAll() {
        this.shownViewers.forEach(player -> {
            super.setCenter(0, 0);
            super.setSize(59999998);
            HCore.sendPacket(player, new PacketPlayOutWorldBorder(this, PacketPlayOutWorldBorder.EnumWorldBorderAction.INITIALIZE));
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
    public void setSize(double size, long time) {
        super.transitionSizeBetween(this.getSize(), size, time);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update() {
        if (this.color == BorderColor.BLUE)
            super.transitionSizeBetween(super.getSize(), super.getSize(), Long.MAX_VALUE);
        else if (this.color == BorderColor.GREEN)
            super.transitionSizeBetween(super.getSize(), super.getSize() + 0.1, Long.MAX_VALUE);
        else if (this.color == BorderColor.RED)
            super.transitionSizeBetween(super.getSize(), super.getSize() - 0.1, Long.MAX_VALUE);
        this.shownViewers.forEach(this::show);
    }
}