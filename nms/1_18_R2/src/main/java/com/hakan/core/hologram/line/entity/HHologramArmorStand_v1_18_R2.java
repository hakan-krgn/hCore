package com.hakan.core.hologram.line.entity;

import com.hakan.core.HCore;
import com.hakan.core.hologram.HHologram;
import net.minecraft.network.chat.ChatMessage;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutEntityTeleport;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntityLiving;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import net.minecraft.world.level.World;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;

/**
 * {@inheritDoc}
 */
public final class HHologramArmorStand_v1_18_R2 implements HHologramArmorStand {

    private final HHologram hologram;
    private final EntityArmorStand armorStand;

    /**
     * {@inheritDoc}
     */
    private HHologramArmorStand_v1_18_R2(@Nonnull HHologram hHologram, @Nonnull Location location) {
        this.armorStand = new EntityArmorStand(((CraftWorld) Objects.requireNonNull(location.getWorld())).getHandle(), location.getX(), location.getY(), location.getZ());
        this.armorStand.persistentInvisibility = true; //set invisibility to true
        this.armorStand.b(5, true); //set invisibility to true
        this.armorStand.n(true); //set custom name visibility to true
        this.armorStand.t(true); //set marker to true
        this.armorStand.r(false); //set arms to false
        this.armorStand.s(true); //set no base-plate to true
        this.armorStand.e(true); //set no gravity to true
        this.armorStand.a(true); //set small to true
        this.armorStand.c(114.13f); //set health to 114.13 float
        this.hologram = hHologram;
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public String getText() {
        return Objects.requireNonNull(this.armorStand.Z()).a();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setText(@Nonnull String text) {
        Validate.notNull(text, "text cannot be null");

        this.armorStand.a(new ChatMessage(text));

        HCore.sendPacket(this.hologram.getRenderer().getShownViewersAsPlayer(),
                new PacketPlayOutEntityMetadata(this.armorStand.ae(), this.armorStand.ai(), true));
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
        Validate.notNull(location, "location cannot be null");

        World world = ((CraftWorld) Objects.requireNonNull(location.getWorld())).getHandle();
        if (!world.equals(this.armorStand.s)) this.armorStand.s = world;
        this.armorStand.a(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

        HCore.sendPacket(this.hologram.getRenderer().getShownViewersAsPlayer(),
                new PacketPlayOutEntityTeleport(this.armorStand));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void show(@Nonnull List<Player> players) {
        HCore.sendPacket(Objects.requireNonNull(players, "players cannot be null"),
                new PacketPlayOutSpawnEntityLiving(this.armorStand),
                new PacketPlayOutEntityMetadata(this.armorStand.ae(), this.armorStand.ai(), true),
                new PacketPlayOutEntityTeleport(this.armorStand));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void hide(@Nonnull List<Player> players) {
        HCore.sendPacket(Objects.requireNonNull(players, "players cannot be null"),
                new PacketPlayOutEntityDestroy(this.armorStand.ae()));
    }
}