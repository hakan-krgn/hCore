package com.hakan.core.npc.wrapper;

import com.hakan.core.npc.HNPC;
import com.hakan.core.utils.Validate;
import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.DataWatcherObject;
import net.minecraft.server.v1_16_R3.DataWatcherRegistry;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import org.bukkit.Location;
import org.bukkit.World;

import javax.annotation.Nonnull;

/**
 * HNPCUtils_v1_8_R3 class.
 */
public final class HNPCUtils_v1_16_R3 {

    private final HNPC npc;

    /**
     * Creates a new HNPCUtils_v1_16_R3 instance.
     *
     * @param npc The NPC.
     */
    public HNPCUtils_v1_16_R3(HNPC npc) {
        this.npc = npc;
    }

    /**
     * Creates data watcher for the NPC.
     *
     * @return Data watcher.
     */
    @Nonnull
    public DataWatcher createDataWatcher(EntityPlayer npc) {
        DataWatcher dataWatcher = npc.getDataWatcher();
        dataWatcher.set(new DataWatcherObject<>(16, DataWatcherRegistry.a), (byte) 127);
        return dataWatcher;
    }

    /**
     * This method walks the NPC to the location
     * that is given. It creates a zombie and
     * villager then make target of zombie to
     * villager and teleport NPC to zombie every tick.
     *
     * @param to       The location.
     * @param speed    The speed of the NPC.
     * @param callback The callback when the walking over.
     */
    public void walk(@Nonnull Location to, double speed, @Nonnull Runnable callback) {
        Validate.notNull(to, "location cannot be null!");
        Validate.notNull(callback, "callback cannot be null!");

        Location from = this.npc.getLocation();
        World toWorld = to.getWorld();
        World fromWorld = from.getWorld();

        Validate.isTrue(toWorld == null || !toWorld.equals(fromWorld), "to and from worlds must be the same!");
    }
}