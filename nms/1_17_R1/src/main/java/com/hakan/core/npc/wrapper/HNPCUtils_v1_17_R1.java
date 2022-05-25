package com.hakan.core.npc.wrapper;

import com.hakan.core.HCore;
import com.hakan.core.npc.HNPC;
import com.hakan.core.npc.skin.HNPCSkin;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.network.syncher.DataWatcherObject;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.attributes.AttributeModifiable;
import net.minecraft.world.entity.ai.attributes.GenericAttributes;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import net.minecraft.world.entity.monster.EntityZombie;
import net.minecraft.world.entity.npc.EntityVillager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.CraftServer;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityTargetEvent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

/**
 * HNPCUtils_v1_8_R3 class.
 */
public final class HNPCUtils_v1_17_R1 {

    /**
     * Creates a GameProfile for the NPC.
     *
     * @param name The name of the NPC.
     * @return The GameProfile.
     */
    @Nonnull
    public GameProfile createGameProfile(@Nonnull String name) {
        Objects.requireNonNull(name, "name cannot be null!");

        HNPCSkin skin = HNPCSkin.from(name);
        GameProfile profile = new GameProfile(UUID.randomUUID(), name);
        profile.getProperties().put("textures", new Property("textures", skin.getTexture(), skin.getSignature()));
        return profile;
    }

    /**
     * Creates a new NPC entity.
     *
     * @param name     The name of the NPC.
     * @param location The location of the NPC.
     * @return The NPC entity.
     */
    @Nonnull
    public EntityPlayer createNPC(@Nonnull String name, @Nonnull Location location) {
        Objects.requireNonNull(name, "name cannot be null!");
        Objects.requireNonNull(location, "location cannot be null!");

        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer world = ((CraftWorld) location.getWorld()).getHandle();
        GameProfile profile = this.createGameProfile(name);

        EntityPlayer entityPlayer = new EntityPlayer(server, world, profile);
        entityPlayer.setInvisible(false);
        entityPlayer.setHealth(77.21f);

        return entityPlayer;
    }

    /**
     * Creates an armor stand to
     * hide name of NPC.
     *
     * @param location The location of armor stand.
     * @return Armor stand.
     */
    @Nonnull
    public EntityArmorStand createNameHider(@Nonnull Location location) {
        Objects.requireNonNull(location, "location cannot be null!");

        WorldServer world = ((CraftWorld) location.getWorld()).getHandle();

        EntityArmorStand armorStand = new EntityArmorStand(world, 0, 0, 0);
        armorStand.setMarker(true);
        armorStand.setArms(false);
        armorStand.setBasePlate(false);
        armorStand.setNoGravity(true);
        armorStand.setInvisible(true);
        armorStand.setSmall(true);
        armorStand.setCustomNameVisible(false);

        return armorStand;
    }

    /**
     * Creates data watcher for the NPC.
     *
     * @return Data watcher.
     */
    @Nonnull
    public DataWatcher createDataWatcher() {
        DataWatcher dataWatcher = new DataWatcher(null);
        dataWatcher.set(new DataWatcherObject<>(17, DataWatcherRegistry.a), (byte) 127);
        return dataWatcher;
    }

    /**
     * This method walks the NPC to the location
     * that is given. It creates a zombie and
     * villager then make target of zombie to
     * villager and teleport NPC to zombie every tick.
     *
     * @param npc      The NPC.
     * @param to       The location.
     * @param speed    The speed of the NPC.
     * @param callback The callback when the walking over.
     */
    public void walk(@Nonnull HNPC npc, @Nonnull Location to, double speed, @Nonnull Runnable callback) {
        Objects.requireNonNull(npc, "NPC cannot be null!");
        Objects.requireNonNull(to, "location cannot be null!");
        Objects.requireNonNull(callback, "callback cannot be null!");

        Location from = npc.getLocation();
        if (from.getWorld() != to.getWorld())
            throw new IllegalArgumentException("cannot walk between different worlds!");

        WorldServer world = ((CraftWorld) to.getWorld()).getHandle();

        EntityVillager villager = new EntityVillager(EntityTypes.aV, world);
        villager.setLocation(to.getX(), to.getY(), to.getZ(), to.getYaw(), to.getPitch());
        villager.setInvisible(true);
        villager.setSilent(true);

        EntityZombie zombie = new EntityZombie(world);
        zombie.setLocation(from.getX(), from.getY(), from.getZ(), from.getYaw(), from.getPitch());
        zombie.setInvisible(false);
        zombie.setSilent(true);


        AttributeModifiable villagerAttribute = villager.getAttributeInstance(GenericAttributes.d);
        if (villagerAttribute != null)
            villagerAttribute.setValue(speed);

        AttributeModifiable zombieAttribute = zombie.getAttributeInstance(GenericAttributes.d);
        if (zombieAttribute != null)
            zombieAttribute.setValue(speed);


        world.addEntity(zombie, CreatureSpawnEvent.SpawnReason.CUSTOM);
        world.addEntity(villager, CreatureSpawnEvent.SpawnReason.CUSTOM);

        zombie.setGoalTarget(villager, EntityTargetEvent.TargetReason.CUSTOM, true);

        HCore.sendPacket(new ArrayList<>(Bukkit.getOnlinePlayers()),
                new PacketPlayOutEntityDestroy(zombie.getId()),
                new PacketPlayOutEntityDestroy(villager.getId()));

        HCore.syncScheduler().every(1)
                .run((task) -> {
                    Location zombieLocation = zombie.getBukkitEntity().getLocation();

                    if (zombieLocation.distance(to) < 1) {
                        zombie.killEntity();
                        villager.killEntity();
                        npc.setLocation(to);
                        callback.run();
                        task.cancel();
                        return;
                    }

                    npc.setLocation(zombieLocation);
                });
    }
}