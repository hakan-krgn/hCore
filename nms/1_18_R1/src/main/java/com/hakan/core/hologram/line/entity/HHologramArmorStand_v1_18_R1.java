package com.hakan.core.hologram.line.entity;

import com.hakan.core.HCore;
import com.hakan.core.hologram.HHologram;
import net.minecraft.core.Vector3f;
import net.minecraft.network.chat.ChatMessage;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutEntityTeleport;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntityLiving;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import net.minecraft.world.level.World;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R1.CraftWorld;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;

/**
 * {@inheritDoc}
 */
public class HHologramArmorStand_v1_18_R1 extends EntityArmorStand implements HHologramArmorStand {

    private final HHologram hologram;

    /**
     * {@inheritDoc}
     */
    private HHologramArmorStand_v1_18_R1(@Nonnull HHologram hHologram, @Nonnull Location location) {
        super(((CraftWorld) Objects.requireNonNull(location.getWorld())).getHandle(), location.getX(), location.getY(), location.getZ());
        super.persistentInvisibility = true; //set invisibility to true
        super.b(5, true); //set invisibility to true
        super.n(true); //set custom name visibility to true
        super.t(true); //set marker to true
        super.r(false); //set arms to false
        super.s(true); //set no base-plate to true
        super.e(true); //set no gravity to true
        super.a(true); //set small to true
        super.c(114.13f); //set health to 114.13 float
        this.hologram = hHologram;
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public String getText() {
        return Objects.requireNonNull(super.Z()).a();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setText(@Nonnull String text) {
        Validate.notNull(text, "text cannot be null");

        super.a(new ChatMessage(text));

        HCore.sendPacket(this.hologram.getRenderer().getShownViewersAsPlayer(),
                new PacketPlayOutEntityMetadata(super.ae(), super.ai(), true));
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public Location getLocation() {
        return super.getBukkitEntity().getLocation();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLocation(@Nonnull Location location) {
        Validate.notNull(location, "location cannot be null");

        World world = ((CraftWorld) Objects.requireNonNull(location.getWorld())).getHandle();
        if (!world.equals(super.t)) super.t = world;
        super.a(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

        HCore.sendPacket(this.hologram.getRenderer().getShownViewersAsPlayer(),
                new PacketPlayOutEntityTeleport(this));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void show(@Nonnull List<Player> players) {
        Validate.notNull(players, "players cannot be null");

        HCore.sendPacket(players,
                new PacketPlayOutSpawnEntityLiving(this),
                new PacketPlayOutEntityMetadata(super.ae(), super.ai(), true),
                new PacketPlayOutEntityTeleport(this));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void hide(@Nonnull List<Player> players) {
        Validate.notNull(players, "players cannot be null");

        HCore.sendPacket(players,
                new PacketPlayOutEntityDestroy(super.ae()));
    }

    @Override
    public Vector3f x() {
        return super.cj;
    }

    @Override
    public boolean d_() {
        return super.d_();
    }
}