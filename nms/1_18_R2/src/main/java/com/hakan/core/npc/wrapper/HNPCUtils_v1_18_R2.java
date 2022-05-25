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
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityTargetEvent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

/**
 * HNPCUtils_v1_8_R3 class.
 */
public final class HNPCUtils_v1_18_R2 {

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
        entityPlayer.persistentInvisibility = false; //set invinsiblity to true
        entityPlayer.b(5, true); //set invinsiblity to true
        entityPlayer.c(77.21f); //sets health to 77.21f

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
        armorStand.persistentInvisibility = true; //set invinsiblity to true
        armorStand.b(5, true); //set invinsiblity to true
        armorStand.n(false); //set custom name visibility to true
        armorStand.t(true); //set marker to true
        armorStand.r(false); //set arms to false
        armorStand.s(true); //set no base plate to true
        armorStand.e(true); //set no gravity to true
        armorStand.a(true); //set small to true
        armorStand.c(114.13f); //set health to 114.13 float

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
        dataWatcher.a(new DataWatcherObject<>(17, DataWatcherRegistry.a), (byte) 127);
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
        villager.a(to.getX(), to.getY(), to.getZ(), to.getYaw(), to.getPitch()); //set location
        villager.persistentInvisibility = true; //set invinsiblity to true
        villager.b(5, true); //set invinsiblity to true
        villager.d(true); //set silent to true

        EntityZombie zombie = new EntityZombie(world);
        zombie.a(from.getX(), from.getY(), from.getZ(), from.getYaw(), from.getPitch()); //set location
        zombie.persistentInvisibility = true; //set invinsiblity to true
        zombie.b(5, true); //set invinsiblity to true
        zombie.d(true); //set silent to true


        AttributeModifiable villagerAttribute = villager.a(GenericAttributes.d);
        if (villagerAttribute != null)
            villagerAttribute.a(speed);

        AttributeModifiable zombieAttribute = zombie.a(GenericAttributes.d);
        if (zombieAttribute != null)
            zombieAttribute.a(speed);

        world.addFreshEntity(zombie, CreatureSpawnEvent.SpawnReason.CUSTOM);
        world.addFreshEntity(villager, CreatureSpawnEvent.SpawnReason.CUSTOM);

        zombie.setTarget(villager, EntityTargetEvent.TargetReason.CUSTOM, true);

        HCore.sendPacket(new ArrayList<>(Bukkit.getOnlinePlayers()),
                new PacketPlayOutEntityDestroy(zombie.ae()),
                new PacketPlayOutEntityDestroy(villager.ae()));

        HCore.syncScheduler().every(1)
                .run((task) -> {
                    Location zombieLocation = zombie.getBukkitEntity().getLocation();

                    if (zombieLocation.distance(to) < 1) {
                        zombie.ah();
                        villager.ah();
                        npc.setLocation(to);
                        callback.run();
                        task.cancel();
                        return;
                    }

                    npc.setLocation(zombieLocation);
                });
    }
}