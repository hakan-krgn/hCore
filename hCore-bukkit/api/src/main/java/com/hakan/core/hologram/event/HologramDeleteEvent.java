package com.hakan.core.hologram.event;

import com.hakan.core.hologram.Hologram;
import com.hakan.core.utils.Validate;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;

/**
 * HologramSpawnEvent class.
 */
public final class HologramDeleteEvent extends Event {

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


    private final Hologram hologram;

    /**
     * Creates new instance of this class.
     *
     * @param hologram Hologram class.
     */
    public HologramDeleteEvent(@Nonnull Hologram hologram) {
        this.hologram = Validate.notNull(hologram, "hologram cannot be null!");
    }

    /**
     * Gets handler list.
     *
     * @return Handler list.
     */
    @Nonnull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * Gets event hologram.
     *
     * @return Event hologram.
     */
    @Nonnull
    public Hologram getHologram() {
        return this.hologram;
    }
}
