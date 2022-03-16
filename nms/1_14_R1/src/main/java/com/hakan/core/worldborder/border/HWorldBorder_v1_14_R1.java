package com.hakan.core.worldborder.border;

import com.hakan.core.HCore;
import com.hakan.core.worldborder.HWorldBorderHandler;
import net.minecraft.server.v1_14_R1.PacketPlayOutWorldBorder;
import net.minecraft.server.v1_14_R1.WorldBorder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class HWorldBorder_v1_14_R1 extends WorldBorder implements HWorldBorder {

    private HBorderColor color;
    private final Set<Player> shownViewers;

    public HWorldBorder_v1_14_R1(Location location, double size, double damageAmount, double damageBuffer, int warningDistance, int warningTime, HBorderColor color) {
        this.color = color;
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

    @Override
    public void show(Player player) {
        this.shownViewers.add(player);
        HCore.sendPacket(player, new PacketPlayOutWorldBorder(this, PacketPlayOutWorldBorder.EnumWorldBorderAction.INITIALIZE));
    }

    @Override
    public void showAll() {
        Bukkit.getOnlinePlayers().forEach(this::show);
    }

    @Override
    public void hide(Player player) {
        this.shownViewers.remove(player);
        super.setCenter(0, 0);
        super.setSize(59999998);
        HCore.sendPacket(player, new PacketPlayOutWorldBorder(this, PacketPlayOutWorldBorder.EnumWorldBorderAction.INITIALIZE));
    }

    @Override
    public void hideAll() {
        this.shownViewers.forEach(this::hide);
    }

    @Override
    public void delete() {
        this.hideAll();
        HWorldBorderHandler.getContent().remove(this);
    }

    @Override
    public Set<Player> getShownViewers() {
        return this.shownViewers;
    }

    @Override
    public Location getCenter() {
        return new Location(super.world.getWorld(), super.getCenterX(), 64, super.getCenterZ());
    }

    @Override
    public void setCenter(Location location) {
        super.setCenter(location.getX(), location.getZ());
    }

    @Override
    public HBorderColor getColor() {
        return this.color;
    }

    @Override
    public void setColor(HBorderColor hBorderColor) {
        this.color = hBorderColor;
    }

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