package com.hakan.core.hologram.line.text;

import com.hakan.core.HCore;
import com.hakan.core.hologram.Hologram;
import com.hakan.core.utils.Validate;
import net.minecraft.server.v1_14_R1.EntityArmorStand;
import net.minecraft.server.v1_14_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_14_R1.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_14_R1.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_14_R1.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_14_R1.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_14_R1.util.CraftChatMessage;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * {@inheritDoc}
 */
public final class TextLine_v1_14_R1 implements TextLine {

    private String text;
    private final Hologram hologram;
    private final EntityArmorStand click;
    private final EntityArmorStand armorStand;

    /**
     * {@inheritDoc}
     */
    private TextLine_v1_14_R1(@Nonnull Hologram hologram, @Nonnull Location location) {
        World world = ((CraftWorld) Validate.notNull(location.getWorld())).getHandle();
        this.hologram = Validate.notNull(hologram, "hologram class cannot be null!");
        this.click = new EntityArmorStand(world, location.getX(), location.getY(), location.getZ());
        this.armorStand = new EntityArmorStand(world, location.getX(), location.getY(), location.getZ());

        this.click.setArms(false);
        this.click.setBasePlate(false);
        this.click.setNoGravity(false);
        this.click.setInvisible(true);
        this.click.setSmall(false);
        this.click.setCustomNameVisible(false);
        this.click.setHealth(114.13f);

        this.armorStand.setMarker(true);
        this.armorStand.setArms(false);
        this.armorStand.setBasePlate(false);
        this.armorStand.setNoGravity(true);
        this.armorStand.setInvisible(true);
        this.armorStand.setSmall(true);
        this.armorStand.setCustomNameVisible(true);
        this.armorStand.setHealth(114.13f);
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public String getText() {
        return this.text;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setText(@Nonnull String text) {
        this.text = Validate.notNull(text, "text cannot be null!");
        this.armorStand.setCustomName(CraftChatMessage.fromStringOrNull(this.text));
        HCore.sendPacket(this.hologram.getRenderer().getShownPlayers(),
                new PacketPlayOutEntityMetadata(this.armorStand.getId(), this.armorStand.getDataWatcher(), true));
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public Hologram getHologram() {
        return this.hologram;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getEntityID() {
        return this.click.getId();
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public Location getLocation() {
        return this.armorStand.getBukkitEntity().getLocation();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLocation(@Nonnull Location location) {
        Validate.notNull(location, "location cannot be null!");

        World world = ((CraftWorld) Validate.notNull(location.getWorld())).getHandle();
        if (!world.equals(this.click.getWorld())) this.click.spawnIn(world);
        if (!world.equals(this.armorStand.getWorld())) this.armorStand.spawnIn(world);
        this.click.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        this.armorStand.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

        HCore.sendPacket(this.hologram.getRenderer().getShownPlayers(),
                new PacketPlayOutEntityTeleport(this.armorStand),
                new PacketPlayOutEntityTeleport(this.click));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMarker(boolean marker) {
        this.armorStand.setMarker(marker);
        this.click.setMarker(marker);
        HCore.sendPacket(this.hologram.getRenderer().getShownPlayers(),
                new PacketPlayOutEntityMetadata(this.click.getId(), this.click.getDataWatcher(), true),
                new PacketPlayOutEntityMetadata(this.armorStand.getId(), this.armorStand.getDataWatcher(), true));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void show(@Nonnull List<Player> players) {
        HCore.sendPacket(Validate.notNull(players, "players cannot be null!"),
                new PacketPlayOutSpawnEntityLiving(this.armorStand),
                new PacketPlayOutEntityMetadata(this.armorStand.getId(), this.armorStand.getDataWatcher(), true),
                new PacketPlayOutEntityTeleport(this.armorStand),

                new PacketPlayOutSpawnEntityLiving(this.click),
                new PacketPlayOutEntityMetadata(this.click.getId(), this.click.getDataWatcher(), true),
                new PacketPlayOutEntityTeleport(this.click));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void hide(@Nonnull List<Player> players) {
        HCore.sendPacket(Validate.notNull(players, "players cannot be null!"),
                new PacketPlayOutEntityDestroy(this.armorStand.getId()),
                new PacketPlayOutEntityDestroy(this.click.getId()));
    }
}