package com.hakan.core.npc.utils;

import com.hakan.core.npc.HNPC;
import com.hakan.core.npc.entity.HNpcEntity;
import com.hakan.core.utils.ReflectionUtils;
import com.hakan.core.utils.Validate;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * HNpcUtils class to
 * create npc entity.
 */
public final class HNpcUtils {

    /**
     * Creates new HNpcEntity.
     *
     * @param hnpc HNPC instance.
     * @return HNpcEntity object.
     */
    @Nonnull
    public static HNpcEntity createEntity(@Nonnull HNPC hnpc) {
        Validate.notNull(hnpc, "hnpc cannot be null!");
        return ReflectionUtils.newInstance("com.hakan.core.npc.entity.HNpcEntity_%s", new Class[]{HNPC.class}, new Object[]{hnpc});
    }

    /**
     * Gets the nearest
     * player to npc.
     *
     * @param hnpc HNPC instance.
     * @return The nearest player.
     */
    @Nullable
    public static Player getNearestPlayer(@Nonnull HNPC hnpc) {
        Validate.notNull(hnpc, "hnpc cannot be null!");

        Player nearestPlayer = null;

        double distance = Double.MAX_VALUE;
        for (Player player : hnpc.getRenderer().getShownPlayers()) {
            double currentDistance = player.getLocation().distance(hnpc.getLocation());
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

    /**
     * Calculates the yaw angle
     * and pitch angle between
     * two locations as location type
     *
     * @param from From location.
     * @param to   To location.
     * @return The yaw and pitch angle as location.
     */
    public static Location calculateVectorAsLocation(@Nonnull Location from, @Nonnull Location to) {
        Validate.notNull(from, "from cannot be null!");
        Validate.notNull(to, "to cannot be null!");

        double xDiff = to.getX() - from.getX();
        double yDiff = to.getY() - from.getY();
        double zDiff = to.getZ() - from.getZ();

        double yawAngle = -Math.toDegrees(Math.atan(xDiff / zDiff));
        double pitchAngle = -Math.toDegrees(Math.atan(yDiff / Math.sqrt(xDiff * xDiff + zDiff * zDiff)));
        yawAngle = (zDiff < 0) ? yawAngle + 180 : yawAngle;

        return new Location(from.getWorld(), from.getX(), from.getY(), from.getZ(), (float) yawAngle, (float) pitchAngle);
    }
}