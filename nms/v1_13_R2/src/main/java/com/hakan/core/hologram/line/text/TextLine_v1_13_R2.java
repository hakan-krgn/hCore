package com.hakan.core.hologram.line.text;

import com.hakan.core.HCore;
import com.hakan.core.hologram.HHologram;
import com.hakan.core.utils.Validate;
import net.minecraft.server.v1_13_R2.EntityArmorStand;
import net.minecraft.server.v1_13_R2.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_13_R2.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_13_R2.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_13_R2.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_13_R2.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_13_R2.util.CraftChatMessage;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * {@inheritDoc}
 */
public final class TextLine_v1_13_R2 implements TextLine {

    private final HHologram hologram;
    private final EntityArmorStand click;
    private final EntityArmorStand armorStand;

    /**
     * {@inheritDoc}
     */
    private TextLine_v1_13_R2(@Nonnull HHologram hHologram, @Nonnull Location location) {
        World world = ((CraftWorld) Validate.notNull(location.getWorld())).getHandle();
        this.hologram = Validate.notNull(hHologram, "hologram class cannot be null!");
        this.armorStand = new EntityArmorStand(world, location.getX(), location.getY(), location.getZ());
        this.click = new EntityArmorStand(world, location.getX(), location.getY(), location.getZ());

        this.armorStand.setMarker(true);
        this.armorStand.setArms(false);
        this.armorStand.setBasePlate(false);
        this.armorStand.setNoGravity(true);
        this.armorStand.setInvisible(true);
        this.armorStand.setSmall(true);
        this.armorStand.setCustomNameVisible(true);
        this.armorStand.setHealth(114.13f);

        this.click.setInvisible(true);
        this.click.setSmall(true);
        this.click.setMarker(false);
        this.click.setCustomNameVisible(false);
        this.click.setNoGravity(true);
        this.click.setHealth(114.13f);
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public String getText() {
        return Validate.notNull(this.armorStand.getCustomName()).getText();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setText(@Nonnull String text) {
        this.armorStand.setCustomName(CraftChatMessage.fromStringOrNull(Validate.notNull(text, "text cannot be null!")));
        HCore.sendPacket(this.hologram.getRenderer().getShownViewersAsPlayer(),
                new PacketPlayOutEntityMetadata(this.armorStand.getId(), this.armorStand.getDataWatcher(), true));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getClickableEntityID() {
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
        if (!world.equals(this.armorStand.getWorld())) this.armorStand.spawnIn(world);
        this.armorStand.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

        if (!world.equals(this.click.getWorld())) this.click.spawnIn(world);
        this.click.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

        HCore.sendPacket(this.hologram.getRenderer().getShownViewersAsPlayer(),
                new PacketPlayOutEntityTeleport(this.armorStand));
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