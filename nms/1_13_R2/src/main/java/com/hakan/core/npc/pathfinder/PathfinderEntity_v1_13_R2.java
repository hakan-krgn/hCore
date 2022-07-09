package com.hakan.core.npc.pathfinder;

import com.hakan.core.HCore;
import com.hakan.core.listener.HListenerAdapter;
import com.hakan.core.utils.Validate;
import net.minecraft.server.v1_13_R2.EntityPig;
import net.minecraft.server.v1_13_R2.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_13_R2.World;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import org.bukkit.event.player.PlayerJoinEvent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * PathfinderEntity is a
 * custom entity class.
 */
public final class PathfinderEntity_v1_13_R2 extends EntityPig {

    /**
     * Creates a new PathfinderEntity
     * instance and add it to world.
     *
     * @param start        Location to spawn.
     * @param end          Location to end.
     * @param speed        Speed of entity.
     * @param walkRunnable Runnable to run when entity is walking.
     * @param endRunnable  Runnable to run when entity is ended.
     * @return PathfinderEntity_v1_13_R2 instance.
     */
    public static PathfinderEntity_v1_13_R2 create(@Nonnull Location start,
                                                   @Nonnull Location end,
                                                   double speed,
                                                   @Nonnull Consumer<EntityPig> walkRunnable,
                                                   @Nonnull Consumer<EntityPig> endRunnable) {
        Validate.notNull(start, "start location cannot be null!");
        Validate.notNull(start.getWorld(), "start world cannot be null!");

        World world = ((CraftWorld) start.getWorld()).getHandle();
        return new PathfinderEntity_v1_13_R2(world, start, end, speed, walkRunnable, endRunnable);
    }


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
    private PathfinderEntity_v1_13_R2(@Nonnull World world,
                                      @Nonnull Location start,
                                      @Nonnull Location end,
                                      double speed,
                                      @Nonnull Consumer<EntityPig> walkRunnable,
                                      @Nonnull Consumer<EntityPig> endRunnable) {
        super(Validate.notNull(world));

        Validate.notNull(start, "start location cannot be null!");
        Validate.notNull(end, "end location cannot be null!");
        Validate.notNull(walkRunnable, "walk runnable cannot be null!");
        Validate.notNull(endRunnable, "end runnable cannot be null!");

        super.setSilent(true);
        super.setInvisible(true);
        super.setInvulnerable(true);
        super.setCustomNameVisible(false);
        super.setPosition(start.getX(), start.getY(), start.getZ());
        super.setHealth(2.518f);
        world.addEntity(this);

        HListenerAdapter<PlayerJoinEvent> listenerAdapter = HCore.registerEvent(PlayerJoinEvent.class)
                .consumeAsync(event -> HCore.sendPacket(event.getPlayer(), new PacketPlayOutEntityDestroy(super.getId())));
        HCore.sendPacket(new ArrayList<>(Bukkit.getOnlinePlayers()),
                new PacketPlayOutEntityDestroy(super.getId()));

        super.goalSelector.a(2, new PathfinderGoal_v1_13_R2(this, end, speed,
                () -> walkRunnable.accept(this),
                () -> {
                    listenerAdapter.unregister();
                    endRunnable.accept(this);
                }));
    }
}