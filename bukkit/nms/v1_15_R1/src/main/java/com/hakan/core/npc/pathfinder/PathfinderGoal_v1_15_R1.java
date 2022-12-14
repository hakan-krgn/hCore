package com.hakan.core.npc.pathfinder;

import com.hakan.core.utils.Validate;
import net.minecraft.server.v1_15_R1.EntityInsentient;
import net.minecraft.server.v1_15_R1.NavigationAbstract;
import net.minecraft.server.v1_15_R1.PathEntity;
import net.minecraft.server.v1_15_R1.PathfinderGoal;
import org.bukkit.Location;

import javax.annotation.Nonnull;
import java.util.EnumSet;

/**
 * PathfinderGoal is a targeter class.
 */
public final class PathfinderGoal_v1_15_R1 extends PathfinderGoal {

    private final EntityInsentient entity;
    private final Runnable endRunnable;
    private final Runnable walkRunnable;
    private final Location end;
    private final double speed;

    /**
     * Creates a new PathfinderGoal instance.
     *
     * @param entity       Entity to target.
     * @param end          Location to end.
     * @param speed        Speed of entity.
     * @param walkRunnable Runnable to run when entity is walking.
     * @param endRunnable  Runnable to run when entity is ended.
     */
    public PathfinderGoal_v1_15_R1(@Nonnull EntityInsentient entity,
                                   @Nonnull Location end,
                                   double speed,
                                   @Nonnull Runnable walkRunnable,
                                   @Nonnull Runnable endRunnable) {
        this.entity = Validate.notNull(entity, "entity cannot be null!");
        this.end = Validate.notNull(end, "end location cannot be null!");
        this.endRunnable = Validate.notNull(endRunnable, "end runnable cannot be null!");
        this.walkRunnable = Validate.notNull(walkRunnable, "walk runnable cannot be null!");
        this.speed = speed;
        super.a(EnumSet.of(Type.MOVE));
    }

    /**
     * If returns true, c() method runs.
     *
     * @return True.
     */
    @Override
    public boolean a() {
        return true;
    }

    /**
     * Executes the goal.
     */
    @Override
    public void c() {
        NavigationAbstract navigation = this.entity.getNavigation();
        PathEntity pathEntity = navigation.a(this.end.getX(), this.end.getY(), this.end.getZ(), 0);
        if (pathEntity != null) {
            pathEntity.c(0);
            navigation.a(pathEntity, this.speed);
        }
    }

    /**
     * Runs when entity is walking.
     *
     * @return Returns false, if entity is walking.
     */
    @Override
    public boolean b() {
        this.walkRunnable.run();
        return !this.entity.getNavigation().n();
    }

    /**
     * Runnable to run when entity
     * walking is ended.
     */
    @Override
    public void d() {
        if (this.entity.getBukkitEntity().getLocation().distance(this.end) < 1)
            this.endRunnable.run();
    }
}