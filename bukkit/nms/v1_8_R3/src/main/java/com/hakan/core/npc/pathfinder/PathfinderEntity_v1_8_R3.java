package com.hakan.core.npc.pathfinder;

import com.hakan.core.HCore;
import com.hakan.core.listener.ListenerAdapter;
import com.hakan.core.utils.Validate;
import net.minecraft.server.v1_8_R3.EntityPig;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.event.player.PlayerJoinEvent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * PathfinderEntity is a
 * custom entity class.
 */
public final class PathfinderEntity_v1_8_R3 {

    /**
     * Creates a new PathfinderEntity
     * instance and add it to world.
     *
     * @param start        Location to spawn.
     * @param end          Location to end.
     * @param speed        Speed of entity.
     * @param walkRunnable Runnable to run when entity is walking.
     * @param endRunnable  Runnable to run when entity is ended.
     * @return PathfinderEntity_v1_8_R3 instance.
     */
    public static PathfinderEntity_v1_8_R3 create(@Nonnull Location start,
                                                  @Nonnull Location end,
                                                  double speed,
                                                  @Nonnull Consumer<EntityPig> walkRunnable,
                                                  @Nonnull Consumer<EntityPig> endRunnable) {
        Validate.notNull(start, "start location cannot be null!");
        Validate.notNull(start.getWorld(), "start world cannot be null!");

        World world = ((CraftWorld) start.getWorld()).getHandle();
        return new PathfinderEntity_v1_8_R3(world, start, end, speed, walkRunnable, endRunnable);
    }



    private final EntityPig entityPig;

    /**
     * Creates a new PathfinderEntity instance.
     *
     * @param world        World to spawn in.
     * @param start        Location to spawn.
     * @param end          Location to end.
     * @param speed        Speed of entity.
     * @param walkRunnable Runnable to run when entity is walking.
     * @param endRunnable  Runnable to run when entity is ended.
     */
    private PathfinderEntity_v1_8_R3(@Nonnull World world,
                                     @Nonnull Location start,
                                     @Nonnull Location end,
                                     double speed,
                                     @Nonnull Consumer<EntityPig> walkRunnable,
                                     @Nonnull Consumer<EntityPig> endRunnable) {
        this.entityPig = new EntityPig(Validate.notNull(world));

        Validate.notNull(start, "start location cannot be null!");
        Validate.notNull(end, "end location cannot be null!");
        Validate.notNull(walkRunnable, "walk runnable cannot be null!");
        Validate.notNull(endRunnable, "end runnable cannot be null!");

        this.entityPig.getDataWatcher().watch(4, (byte) 1); //set silent to true
        this.entityPig.e(new NBTTagCompound()); //set nbt tag
        this.entityPig.setInvisible(true);
        this.entityPig.setCustomNameVisible(false);
        this.entityPig.setPosition(start.getX(), start.getY(), start.getZ());
        this.entityPig.setHealth(2.518f);
        world.addEntity(this.entityPig);

        ListenerAdapter<PlayerJoinEvent> listenerAdapter = HCore.registerEvent(PlayerJoinEvent.class)
                .consumeAsync(event -> HCore.sendPacket(event.getPlayer(), new PacketPlayOutEntityDestroy(this.entityPig.getId())));
        HCore.sendPacket(new ArrayList<>(Bukkit.getOnlinePlayers()),
                new PacketPlayOutEntityDestroy(this.entityPig.getId()));

        this.entityPig.goalSelector.a(2, new PathfinderGoal_v1_8_R3(this.entityPig, end, speed,
                () -> walkRunnable.accept(this.entityPig),
                () -> {
                    listenerAdapter.unregister();
                    endRunnable.accept(this.entityPig);
                }));
    }
}