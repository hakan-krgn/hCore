package com.hakan.core.npc.utils;

import com.hakan.core.npc.Npc;
import com.hakan.core.npc.entity.NpcEntity;
import com.hakan.core.utils.ReflectionUtils;
import com.hakan.core.utils.Validate;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * NpcUtils class to
 * create npc entity.
 */
public final class NpcUtils {

    /**
     * Creates new NpcEntity.
     *
     * @param npc Npc instance.
     * @return NpcEntity object.
     */
    @Nonnull
    public static NpcEntity createEntity(@Nonnull Npc npc) {
        Validate.notNull(npc, "npc cannot be null!");
        return ReflectionUtils.newInstance("com.hakan.core.npc.entity.NpcEntity_%s",
                new Class[]{Npc.class}, new Object[]{npc});
    }

    /**
     * Gets the nearest
     * player to npc.
     *
     * @param npc Npc instance.
     * @return The nearest player.
     */
    @Nullable
    public static Player getNearestPlayer(@Nonnull Npc npc) {
        Validate.notNull(npc, "npc cannot be null!");

        Player nearestPlayer = null;

        double distance = Double.MAX_VALUE;
        for (Player player : npc.getRenderer().getShownPlayers()) {
            double currentDistance = player.getLocation().distance(npc.getLocation());
            if (currentDistance < distance) {
                distance = currentDistance;
                nearestPlayer = player;
            }
        }
        return nearestPlayer;
    }

    /**
     * Calculates the yaw angle
     * and pitch angle between
     * two locations as location type
     *
     * @param from From location.
     * @param to   To location.
     * @return The yaw and pitch angle as location.
     */
    @Nonnull
    public static Location calculateVectorAsLocation(@Nonnull Location from, @Nonnull Location to) {
        double[] angles = NpcUtils.calculateVector(from, to);
        return new Location(from.getWorld(), from.getX(), from.getY(), from.getZ(), (float) angles[0], (float) angles[1]);
    }

    /**
     * Calculates the yaw angle
     * and pitch angle between
     * two locations.
     *
     * @param from From location.
     * @param to   To location.
     * @return The yaw and pitch angle.
     */
    public static double[] calculateVector(@Nonnull Location from, @Nonnull Location to) {
        Validate.notNull(from, "from cannot be null!");
        Validate.notNull(to, "to cannot be null!");

        double xDiff = to.getX() - from.getX();
        double yDiff = to.getY() - from.getY();
        double zDiff = to.getZ() - from.getZ();

        double yawAngle = -Math.toDegrees(Math.atan(xDiff / zDiff));
        double pitchAngle = -Math.toDegrees(Math.atan(yDiff / Math.sqrt(xDiff * xDiff + zDiff * zDiff)));
        yawAngle = (zDiff < 0) ? yawAngle + 180 : yawAngle;

        return new double[]{yawAngle, pitchAngle};
    }
}
