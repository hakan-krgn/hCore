package com.hakan.core.npc.pathfinder;

import com.hakan.core.HCore;
import com.hakan.core.listener.HListenerAdapter;
import com.hakan.core.utils.Validate;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.animal.EntityPig;
import net.minecraft.world.level.World;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * PathfinderEntity is a
 * custom entity class.
 */
public final class PathfinderEntity_v1_19_R1 {

    /**
     * Creates a new PathfinderEntity
     * instance and add it to world.
     *
     * @param start        Location to spawn.
     * @param end          Location to end.
     * @param speed        Speed of entity.
     * @param walkRunnable Runnable to run when entity is walking.
     * @param endRunnable  Runnable to run when entity is ended.
     * @return PathfinderEntity_v1_19_R1 instance.
     */
    public static PathfinderEntity_v1_19_R1 create(@Nonnull Location start,
                                                   @Nonnull Location end,
                                                   double speed,
                                                   @Nonnull Consumer<EntityPig> walkRunnable,
                                                   @Nonnull Consumer<EntityPig> endRunnable) {
        Validate.notNull(start, "start location cannot be null!");
        Validate.notNull(start.getWorld(), "start world cannot be null!");

        World world = ((CraftWorld) start.getWorld()).getHandle();
        return new PathfinderEntity_v1_19_R1(world, start, end, speed, walkRunnable, endRunnable);
    }


    private final EntityPig pig;

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
    private PathfinderEntity_v1_19_R1(@Nonnull World world,
                                      @Nonnull Location start,
                                      @Nonnull Location end,
                                      double speed,
                                      @Nonnull Consumer<EntityPig> walkRunnable,
                                      @Nonnull Consumer<EntityPig> endRunnable) {
        this.pig = new EntityPig(EntityTypes.aq, Validate.notNull(world)) {
            @Override
            protected void u() {

            }
        };

        Validate.notNull(start, "start location cannot be null!");
        Validate.notNull(end, "end location cannot be null!");
        Validate.notNull(walkRunnable, "walk runnable cannot be null!");
        Validate.notNull(endRunnable, "end runnable cannot be null!");

        this.pig.d(true); //set silent to true
        this.pig.persistentInvisibility = true; //set persistent invisibility to true
        this.pig.b(5, true); //set invisibility to true
        this.pig.m(true); //set invulnerable to true
        this.pig.n(false); //set custom name visible to false
        this.pig.a(start.getX(), start.getY(), start.getZ());
        world.addFreshEntity(this.pig, CreatureSpawnEvent.SpawnReason.CUSTOM);

        HListenerAdapter<PlayerJoinEvent> listenerAdapter = HCore.registerEvent(PlayerJoinEvent.class)
                .consumeAsync(event -> HCore.sendPacket(event.getPlayer(), new PacketPlayOutEntityDestroy(this.pig.ae())));
        HCore.sendPacket(new ArrayList<>(Bukkit.getOnlinePlayers()),
                new PacketPlayOutEntityDestroy(this.pig.ae()));

        this.pig.bS.a(2, new PathfinderGoal_v1_19_R1(this.pig, end, speed,
                () -> walkRunnable.accept(this.pig),
                () -> {
                    listenerAdapter.unregister();
                    endRunnable.accept(this.pig);
                }));
    }
}