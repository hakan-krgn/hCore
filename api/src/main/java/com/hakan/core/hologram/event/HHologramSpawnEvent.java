package com.hakan.core.hologram.event;

import com.hakan.core.hologram.HHologram;
import com.hakan.core.utils.Validate;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;

/**
 * HHologramSpawnEvent class.
 */
public final class HHologramSpawnEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    /**
     * Gets handler list.
     *
     * @return Handler list.
     */
    @Nonnull
    public static HandlerList getHandlerList() {
        return handlers;
    }


    private final HHologram hologram;

    /**
     * Creates new instance of this class.
     *
     * @param hologram HHologram class.
     */
    public HHologramSpawnEvent(@Nonnull HHologram hologram) {
        this.hologram = Validate.notNull(hologram, "hologram cannot be null!");
    }

    /**
     * Gets handler list.
     *
     * @return Handler list.
     */
    @Nonnull
    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * Gets event hologram.
     *
     * @return Event hologram.
     */
    @Nonnull
    public HHologram getHologram() {
        return this.hologram;
    }
}