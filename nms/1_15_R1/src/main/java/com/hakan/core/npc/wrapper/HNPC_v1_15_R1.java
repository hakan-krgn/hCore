package com.hakan.core.npc.wrapper;

import com.hakan.core.HCore;
import com.hakan.core.npc.HNPC;
import com.hakan.core.npc.HNPCHandler;
import com.hakan.core.npc.types.HNPCEquipmentType;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_15_R1.EntityArmorStand;
import net.minecraft.server.v1_15_R1.EntityPlayer;
import net.minecraft.server.v1_15_R1.MinecraftServer;
import net.minecraft.server.v1_15_R1.PacketPlayOutEntity;
import net.minecraft.server.v1_15_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_15_R1.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_15_R1.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_15_R1.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_15_R1.PacketPlayOutMount;
import net.minecraft.server.v1_15_R1.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_15_R1.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_15_R1.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_15_R1.PlayerInteractManager;
import net.minecraft.server.v1_15_R1.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
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
public class HNPC_v1_15_R1 extends HNPC {

    private final EntityPlayer npc;
    private final EntityArmorStand armorStand;
    private final MinecraftServer server;
    private final WorldServer world;
    private final GameProfile gameProfile;

    /**
     * {@inheritDoc}
     */
    public HNPC_v1_15_R1(@Nonnull String id,
                         @Nonnull Location location,
                         @Nonnull List<String> lines) {
        super(id, location, lines);

        this.server = ((CraftServer) Bukkit.getServer()).getServer();
        this.world = ((CraftWorld) location.getWorld()).getHandle();
        this.gameProfile = new GameProfile(UUID.randomUUID(), id);

        this.npc = new EntityPlayer(this.server, this.world, this.gameProfile, new PlayerInteractManager(this.world));
        this.npc.setInvisible(false);
        this.npc.setHealth(77.21f);


        this.armorStand = new EntityArmorStand(this.world, 0, 0, 0);
        this.armorStand.setMarker(true);
        this.armorStand.setArms(false);
        this.armorStand.setBasePlate(false);
        this.armorStand.setNoGravity(true);
        this.armorStand.setInvisible(true);
        this.armorStand.setSmall(true);
        this.armorStand.setCustomNameVisible(false);

        this.setLocation(location);
    }

    /**
     * {@inheritDoc}
     */
    public HNPC_v1_15_R1(@Nonnull String id,
                         @Nonnull Location location,
                         @Nonnull List<String> lines,
                         @Nonnull Set<UUID> viewers) {
        super(id, location, lines, viewers);

        this.server = ((CraftServer) Bukkit.getServer()).getServer();
        this.world = ((CraftWorld) location.getWorld()).getHandle();
        this.gameProfile = new GameProfile(UUID.randomUUID(), id);

        this.npc = new EntityPlayer(this.server, this.world, this.gameProfile, new PlayerInteractManager(this.world));
        this.npc.setInvisible(false);
        this.npc.setHealth(77.21f);

        this.armorStand = new EntityArmorStand(this.world, 0, 0, 0);
        this.armorStand.setMarker(true);
        this.armorStand.setArms(false);
        this.armorStand.setBasePlate(false);
        this.armorStand.setNoGravity(true);
        this.armorStand.setInvisible(true);
        this.armorStand.setSmall(true);
        this.armorStand.setCustomNameVisible(false);

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

        this.npc.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        super.renderer.setLocation(location);
        super.hologram.setLocation(location);

        HCore.sendPacket(super.renderer.getShownViewersAsPlayer(),
                new PacketPlayOutEntity.PacketPlayOutEntityLook(this.npc.getId(), (byte) (location.getYaw() % 360f * 256f / 360f), (byte) (location.getPitch() % 360f * 256f / 360f), false),
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

        this.npc.passengers.clear();
        this.npc.passengers.add(this.armorStand);

        HCore.sendPacket(players,
                new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, this.npc),
                new PacketPlayOutNamedEntitySpawn(this.npc),
                new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, this.npc),
                new PacketPlayOutEntityMetadata(this.npc.getId(), this.npc.getDataWatcher(), true));

        //todo add equipment packets

        HCore.sendPacket(players,
                new PacketPlayOutSpawnEntityLiving(this.armorStand),
                new PacketPlayOutEntityMetadata(this.armorStand.getId(), this.armorStand.getDataWatcher(), true),
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
                new PacketPlayOutEntityDestroy(this.npc.getId()),
                new PacketPlayOutEntityDestroy(this.armorStand.getId()));
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