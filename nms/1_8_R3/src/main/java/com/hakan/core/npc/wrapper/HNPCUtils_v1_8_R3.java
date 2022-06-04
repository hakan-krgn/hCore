package com.hakan.core.npc.wrapper;

import com.hakan.core.HCore;
import com.hakan.core.npc.HNPC;
import com.hakan.core.npc.skin.HNPCSkin;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_8_R3.AttributeInstance;
import net.minecraft.server.v1_8_R3.DataWatcher;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.EntityVillager;
import net.minecraft.server.v1_8_R3.EntityZombie;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PlayerInteractManager;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftVillager;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftZombie;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

/**
 * HNPCUtils_v1_8_R3 class.
 */
public final class HNPCUtils_v1_8_R3 {

    /**
     * Creates a GameProfile for the NPC.
     *
     * @param skin The name of the NPC.
     * @return The GameProfile.
     */
    @Nonnull
    public GameProfile createGameProfile(@Nonnull HNPCSkin skin) {
        Objects.requireNonNull(skin, "name cannot be null!");

        GameProfile profile = new GameProfile(UUID.randomUUID(), UUID.randomUUID().toString().substring(0, 5));
        profile.getProperties().put("textures", new Property("textures", skin.getTexture(), skin.getSignature()));
        return profile;
    }

    /**
     * Creates a new NPC entity.
     *
     * @param skin     The name of the NPC.
     * @param location The location of the NPC.
     * @return The NPC entity.
     */
    @Nonnull
    public EntityPlayer createNPC(@Nonnull HNPCSkin skin, @Nonnull Location location) {
        Objects.requireNonNull(skin, "skin cannot be null!");
        Objects.requireNonNull(location, "location cannot be null!");

        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer world = ((CraftWorld) location.getWorld()).getHandle();
        GameProfile profile = this.createGameProfile(skin);

        EntityPlayer entityPlayer = new EntityPlayer(server, world, profile, new PlayerInteractManager(world));
        entityPlayer.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
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
        armorStand.getDataWatcher().watch(10, (byte) 16);
        armorStand.b(new NBTTagCompound());
        armorStand.setArms(false);
        armorStand.setBasePlate(false);
        armorStand.setGravity(false);
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
        dataWatcher.watch(10, (byte) 127);
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
        World toWorld = to.getWorld();
        World fromWorld = from.getWorld();

        if (toWorld == null || !toWorld.equals(fromWorld))
            throw new IllegalArgumentException("cannot walk between different worlds!");

        Villager villager = toWorld.spawn(to, Villager.class);
        villager.setCustomNameVisible(false);
        villager.setHealth(11.9123165d);

        Zombie zombie = fromWorld.spawn(from, Zombie.class);
        zombie.getEquipment().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
        zombie.setCustomNameVisible(false);
        zombie.setHealth(11.9123165d);
        zombie.setTarget(villager);

        EntityZombie entityZombie = ((CraftZombie) zombie).getHandle();
        EntityVillager entityVillager = ((CraftVillager) villager).getHandle();
        entityZombie.b(true);
        entityVillager.b(true);

        AttributeInstance attribute1 = entityZombie.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED);
        AttributeInstance attribute2 = entityZombie.getAttributeInstance(GenericAttributes.FOLLOW_RANGE);
        AttributeInstance attribute3 = entityVillager.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED);
        if (attribute1 != null) attribute1.setValue(speed);
        if (attribute2 != null) attribute2.setValue(200);
        if (attribute3 != null) attribute3.setValue(0);

        HCore.sendPacket(new ArrayList<>(Bukkit.getOnlinePlayers()),
                new PacketPlayOutEntityDestroy(zombie.getEntityId(), villager.getEntityId()));

        HCore.syncScheduler().every(1)
                .run((task) -> {
                    Location zombieLocation = zombie.getLocation();

                    if (zombie.getTarget() == null || !zombie.getTarget().equals(villager))
                        zombie.setTarget(villager);

                    if (zombieLocation.distance(to) < 1 || npc.isDead()) {
                        zombie.remove();
                        villager.remove();
                        npc.setLocation(to);
                        callback.run();
                        task.cancel();
                        return;
                    }

                    npc.setLocation(zombieLocation);
                });
    }
}