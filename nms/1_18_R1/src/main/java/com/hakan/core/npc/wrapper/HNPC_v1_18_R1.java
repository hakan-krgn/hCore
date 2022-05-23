package com.hakan.core.npc.wrapper;

import com.google.common.collect.ImmutableList;
import com.hakan.core.HCore;
import com.hakan.core.npc.HNPC;
import com.hakan.core.npc.HNPCHandler;
import com.hakan.core.npc.types.HNPCEquipmentType;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.protocol.game.PacketPlayOutEntity;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityHeadRotation;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutEntityTeleport;
import net.minecraft.network.protocol.game.PacketPlayOutMount;
import net.minecraft.network.protocol.game.PacketPlayOutNamedEntitySpawn;
import net.minecraft.network.protocol.game.PacketPlayOutPlayerInfo;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R1.CraftServer;
import org.bukkit.craftbukkit.v1_18_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * {@inheritDoc}
 */
public class HNPC_v1_18_R1 extends HNPC {

    private final EntityPlayer npc;
    private final EntityArmorStand armorStand;
    private final MinecraftServer server;
    private final WorldServer world;
    private final GameProfile gameProfile;

    /**
     * {@inheritDoc}
     */
    public HNPC_v1_18_R1(@Nonnull String id,
                         @Nonnull Location location,
                         @Nonnull List<String> lines) {
        super(id, location, lines);

        this.server = ((CraftServer) Bukkit.getServer()).getServer();
        this.world = ((CraftWorld) location.getWorld()).getHandle();
        this.gameProfile = new GameProfile(UUID.randomUUID(), id);

        this.npc = new EntityPlayer(this.server, this.world, this.gameProfile);
        this.npc.persistentInvisibility = false; //set invinsiblity to true
        this.npc.b(5, true); //set invinsiblity to true
        this.npc.c(77.21f); //sets health to 77.21f

        this.armorStand = new EntityArmorStand(this.world, 0, 0, 0);
        this.armorStand.persistentInvisibility = true; //set invinsiblity to true
        this.armorStand.b(5, true); //set invinsiblity to true
        this.armorStand.n(false); //set custom name visibility to true
        this.armorStand.t(true); //set marker to true
        this.armorStand.r(false); //set arms to false
        this.armorStand.s(true); //set no base plate to true
        this.armorStand.e(true); //set no gravity to true
        this.armorStand.a(true); //set small to true
        this.armorStand.c(114.13f); //set health to 114.13 float

        this.setLocation(location);
    }

    /**
     * {@inheritDoc}
     */
    public HNPC_v1_18_R1(@Nonnull String id,
                         @Nonnull Location location,
                         @Nonnull List<String> lines,
                         @Nonnull Set<UUID> viewers) {
        super(id, location, lines, viewers);

        this.server = ((CraftServer) Bukkit.getServer()).getServer();
        this.world = ((CraftWorld) location.getWorld()).getHandle();
        this.gameProfile = new GameProfile(UUID.randomUUID(), id);

        this.npc = new EntityPlayer(this.server, this.world, this.gameProfile);
        this.npc.persistentInvisibility = false; //set invinsiblity to true
        this.npc.b(5, true); //set invinsiblity to true
        this.npc.c(77.21f); //sets health to 77.21f

        this.armorStand = new EntityArmorStand(this.world, 0, 0, 0);
        this.armorStand.persistentInvisibility = true; //set invinsiblity to true
        this.armorStand.b(5, true); //set invinsiblity to true
        this.armorStand.n(false); //set custom name visibility to true
        this.armorStand.t(true); //set marker to true
        this.armorStand.r(false); //set arms to false
        this.armorStand.s(true); //set no base plate to true
        this.armorStand.e(true); //set no gravity to true
        this.armorStand.a(true); //set small to true
        this.armorStand.c(114.13f); //set health to 114.13 float

        this.setLocation(location);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @Nonnull HNPC move(@Nonnull Location to, double speed) {
        Objects.requireNonNull(to, "to location cannot be null!");

        super.walking = true;
        //todo move

        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public HNPC setLocation(@Nonnull Location location) {
        Objects.requireNonNull(location, "location cannot be null!");

        this.npc.a(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        super.renderer.setLocation(location);
        super.hologram.setLocation(location);

        HCore.sendPacket(super.renderer.getShownViewersAsPlayer(),
                new PacketPlayOutEntity.PacketPlayOutEntityLook(this.npc.ae(), (byte) (location.getYaw() % 360f * 256f / 360f), (byte) (location.getPitch() % 360f * 256f / 360f), false),
                new PacketPlayOutEntityHeadRotation(this.npc, (byte) (location.getYaw() * 256f / 360f)),
                new PacketPlayOutEntityTeleport(this.npc));

        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public HNPC setSkin(@Nonnull String username) {
        Objects.requireNonNull(username, "username cannot be null!");

        this.hide(super.renderer.getShownViewersAsPlayer());
        //todo set skin
        this.show(super.renderer.getShownViewersAsPlayer());

        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public HNPC setEquipment(@Nonnull HNPCEquipmentType equipment, @Nonnull ItemStack itemStack) {
        Objects.requireNonNull(equipment, "equipment type cannot be null!");
        Objects.requireNonNull(itemStack, "itemStack type cannot be null!");

        super.equipments.put(equipment, itemStack);

        //todo fix equipment packets

        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public HNPC show(@Nonnull List<Player> players) {
        Objects.requireNonNull(players, "players cannot be null!");

        ((Entity) this.npc).au = ImmutableList.<Entity>builder().add(this.armorStand).build();

        HCore.sendPacket(players,
                new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a, this.npc),
                new PacketPlayOutNamedEntitySpawn(this.npc),
                new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.e, this.npc),
                new PacketPlayOutEntityMetadata(this.npc.ae(), this.npc.ai(), true));

        //todo add equipment packets

        HCore.sendPacket(players,
                new PacketPlayOutSpawnEntityLiving(this.armorStand),
                new PacketPlayOutEntityMetadata(this.armorStand.ae(), this.armorStand.ai(), true),
                new PacketPlayOutEntityTeleport(this.armorStand),
                new PacketPlayOutMount(this.npc));

        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public HNPC hide(@Nonnull List<Player> players) {
        HCore.sendPacket(Objects.requireNonNull(players, "players cannot be null!"),
                new PacketPlayOutEntityDestroy(this.npc.ae()),
                new PacketPlayOutEntityDestroy(this.armorStand.ae()));
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public HNPC delete() {
        HNPCHandler.getContent().remove(super.id);
        super.hologram.delete();
        super.renderer.delete();
        super.walking = false;
        return this.hide(super.renderer.getShownViewersAsPlayer());
    }
}