package com.hakan.core.hologram.line.item;

import com.hakan.core.HCore;
import com.hakan.core.hologram.Hologram;
import com.hakan.core.utils.Validate;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutEntityTeleport;
import net.minecraft.network.protocol.game.PacketPlayOutMount;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntityLiving;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import net.minecraft.world.entity.item.EntityItem;
import net.minecraft.world.level.World;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * {@inheritDoc}
 */
public final class ItemLine_v1_17_R1 implements ItemLine {

    private World world;
    private ItemStack item;
    private final Hologram hologram;
    private final EntityItem nmsItem;
    private final EntityArmorStand armorStand;

    /**
     * {@inheritDoc}
     */
    private ItemLine_v1_17_R1(@Nonnull Hologram hologram, @Nonnull Location location) {
        this.world = ((CraftWorld) Validate.notNull(location.getWorld())).getHandle();
        this.hologram = Validate.notNull(hologram, "hologram class cannot be null!");
        this.armorStand = new EntityArmorStand(this.world, location.getX(), location.getY(), location.getZ());
        this.nmsItem = new EntityItem(this.world, location.getX(), location.getY(), location.getZ(), CraftItemStack.asNMSCopy(new ItemStack(Material.STONE)));
        this.nmsItem.a(this.armorStand, true);

        this.armorStand.setArms(false);
        this.armorStand.setBasePlate(false);
        this.armorStand.setNoGravity(true);
        this.armorStand.setInvisible(true);
        this.armorStand.setSmall(true);
        this.armorStand.setCustomNameVisible(false);
        this.armorStand.setHealth(114.13f);
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public ItemStack getItem() {
        return this.item;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setItem(@Nonnull ItemStack item) {
        this.item = Validate.notNull(item, "item cannot be null!");
        this.nmsItem.setItemStack(CraftItemStack.asNMSCopy(this.item));
        HCore.sendPacket(this.hologram.getRenderer().getShownPlayers(),
                new PacketPlayOutEntityMetadata(this.nmsItem.getId(), this.nmsItem.getDataWatcher(), true));
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
        return this.armorStand.getId();
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public Location getLocation() {
        return this.armorStand.getBukkitEntity()
                .getLocation().add(0, 0.48, 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLocation(@Nonnull Location location) {
        Validate.notNull(location, "location cannot be null!");

        this.world = ((CraftWorld) Validate.notNull(location.getWorld())).getHandle();
        if (!this.world.equals(this.armorStand.getWorld())) this.armorStand.t = this.world;
        this.armorStand.setLocation(location.getX(), location.getY() - 0.48, location.getZ(), location.getYaw(), location.getPitch());

        HCore.sendPacket(this.hologram.getRenderer().getShownPlayers(),
                new PacketPlayOutEntityTeleport(this.armorStand));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMarker(boolean marker) {
        this.armorStand.setMarker(marker);
        HCore.sendPacket(this.hologram.getRenderer().getShownPlayers(),
                new PacketPlayOutEntityMetadata(this.armorStand.getId(), this.armorStand.getDataWatcher(), true));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void show(@Nonnull List<Player> players) {
        if (this.nmsItem != null) {
            this.nmsItem.a(this.armorStand, true);

            HCore.sendPacket(Validate.notNull(players, "players cannot be null!"),
                    new PacketPlayOutSpawnEntityLiving(this.armorStand),
                    new PacketPlayOutEntityMetadata(this.armorStand.getId(), this.armorStand.getDataWatcher(), true),
                    new PacketPlayOutEntityTeleport(this.armorStand),

                    new PacketPlayOutEntityDestroy(this.nmsItem.getId()),
                    new PacketPlayOutSpawnEntity(this.nmsItem),
                    new PacketPlayOutEntityTeleport(this.nmsItem),
                    new PacketPlayOutEntityMetadata(this.nmsItem.getId(), this.nmsItem.getDataWatcher(), true),
                    new PacketPlayOutMount(this.armorStand));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void hide(@Nonnull List<Player> players) {
        HCore.sendPacket(Validate.notNull(players, "players cannot be null!"),
                new PacketPlayOutEntityDestroy(this.nmsItem.getId()),
                new PacketPlayOutEntityDestroy(this.armorStand.getId()));
    }
}