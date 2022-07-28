package com.hakan.core.hologram.line.text;

import com.hakan.core.HCore;
import com.hakan.core.hologram.HHologram;
import com.hakan.core.utils.Validate;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutEntityTeleport;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import net.minecraft.world.level.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R2.util.CraftChatMessage;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * {@inheritDoc}
 */
public final class TextLine_v1_18_R2 implements TextLine {

    private final HHologram hologram;
    private final EntityArmorStand click;
    private final EntityArmorStand armorStand;

    /**
     * {@inheritDoc}
     */
    private TextLine_v1_18_R2(@Nonnull HHologram hHologram, @Nonnull Location location) {
        World world = ((CraftWorld) Validate.notNull(location.getWorld())).getHandle();
        this.hologram = Validate.notNull(hHologram, "hologram class cannot be null!");
        this.armorStand = new EntityArmorStand(world, location.getX(), location.getY(), location.getZ());
        this.click = new EntityArmorStand(world, location.getX(), location.getY(), location.getZ());

        this.armorStand.persistentInvisibility = true; //set invisibility to true
        this.armorStand.b(5, true); //set invisibility to true
        this.armorStand.n(true); //set custom name visibility to true
        this.armorStand.t(true); //set marker to true
        this.armorStand.r(false); //set arms to false
        this.armorStand.s(true); //set no base-plate to true
        this.armorStand.e(true); //set no gravity to true
        this.armorStand.a(true); //set small to true
        this.armorStand.c(114.13f); //set health to 114.13 float

        this.click.persistentInvisibility = true; //set invisibility to true
        this.click.b(5, true); //set invisibility to true
        this.click.a(true); //set small to true
        this.click.t(false); //set marker to false
        this.click.n(false); //set custom name visibility to false
        this.click.e(true); //set no gravity to true
        this.click.c(114.13f); //set health to 114.13 float
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public String getText() {
        return Validate.notNull(this.armorStand.Z()).a();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setText(@Nonnull String text) {
        this.armorStand.a(CraftChatMessage.fromStringOrNull(Validate.notNull(text, "text cannot be null!")));
        HCore.sendPacket(this.hologram.getRenderer().getShownViewersAsPlayer(),
                new PacketPlayOutEntityMetadata(this.armorStand.ae(), this.armorStand.ai(), true));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HHologram getHologram() {
        return this.hologram;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getClickableEntityID() {
        return this.click.ae();
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
        if (!world.equals(this.armorStand.s)) this.armorStand.s = world;
        this.armorStand.a(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

        if (!world.equals(this.click.s)) this.click.s = world;
        this.click.a(location.getX(), location.getY() + 0.24, location.getZ(), location.getYaw(), location.getPitch());

        HCore.sendPacket(this.hologram.getRenderer().getShownViewersAsPlayer(),
                new PacketPlayOutEntityTeleport(this.armorStand),
                new PacketPlayOutEntityTeleport(this.click));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void show(@Nonnull List<Player> players) {
        HCore.sendPacket(Validate.notNull(players, "players cannot be null!"),
                new PacketPlayOutSpawnEntity(this.armorStand),
                new PacketPlayOutEntityMetadata(this.armorStand.ae(), this.armorStand.ai(), true),
                new PacketPlayOutEntityTeleport(this.armorStand),

                new PacketPlayOutSpawnEntity(this.click),
                new PacketPlayOutEntityMetadata(this.click.ae(), this.click.ai(), true),
                new PacketPlayOutEntityTeleport(this.click));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void hide(@Nonnull List<Player> players) {
        HCore.sendPacket(Validate.notNull(players, "players cannot be null!"),
                new PacketPlayOutEntityDestroy(this.armorStand.ae()),
                new PacketPlayOutEntityDestroy(this.click.ae()));
    }
}